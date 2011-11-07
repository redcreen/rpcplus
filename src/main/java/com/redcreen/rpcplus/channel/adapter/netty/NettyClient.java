package com.redcreen.rpcplus.channel.adapter.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.redcreen.rpcplus.channel.ChannelException;
import com.redcreen.rpcplus.channel.ChannelHandler;
import com.redcreen.rpcplus.channel.Future;
import com.redcreen.rpcplus.channel.adapter.Client;
import com.redcreen.rpcplus.channel.support.ChannelUtil;
import com.redcreen.rpcplus.support.Constants.ChannelConstants;
import com.redcreen.rpcplus.support.URL;
import com.redcreen.rpcplus.util.ExecutorUtils;
import com.redcreen.rpcplus.util.NetUtils;
import com.redcreen.rpcplus.util.URLUtils;

public class NettyClient extends Client {

    private static final ChannelFactory channelFactory = initChannelFactory();
    private ClientBootstrap             bootstrap;

    private static ChannelFactory initChannelFactory() {
        return new NioClientSocketChannelFactory(
                ExecutorUtils.newCachedExecutor("NettyClientBoss"),
                ExecutorUtils.newCachedExecutor("NettyClientWorker"),
                ChannelConstants.IO_THREADS_DEFAULT);
    }

    /**
     * @param handler
     * @param url
     * @throws ChannelException
     */
    public NettyClient(ChannelHandler handler, URL url) throws ChannelException {
        super(url, handler);
    }

    @Override
    protected void doOpen() throws ChannelException {
        bootstrap = new ClientBootstrap(channelFactory);
        // config
        // @see org.jboss.netty.channel.socket.SocketChannelConfig
        bootstrap.setOption("keepAlive", true);
        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("connectTimeoutMillis", URLUtils.getTimeout(getURL()));
        final NettyHandlerAdpater nettyHandler = new NettyHandlerAdpater();
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            public ChannelPipeline getPipeline() {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("handler", nettyHandler);
                return pipeline;
            }
        });
    }

    @Override
    protected void doConnect() throws ChannelException {
        ChannelFuture future = bootstrap.connect(getConnectAddress());
        try {
            boolean ret = future.awaitUninterruptibly(URLUtils.getTimeout(getURL()),
                    TimeUnit.MILLISECONDS);

            if (ret && future.isSuccess()) {
                com.redcreen.rpcplus.channel.Channel oldChannel = channel;
                if (oldChannel != null) {
                    try {
                        oldChannel.close(URLUtils.getCloseTimeout(url));
                    } catch (Throwable t) {
                        logger.error("old channel close error.", t);
                    }
                }
                Channel newChannel = future.getChannel();
                newChannel.setInterestOps(Channel.OP_READ_WRITE);
                com.redcreen.rpcplus.channel.Channel channelApater = new NettyChannelAdpater(
                        newChannel, handler, url);
                channel = channelApater;
            } else if (future.getCause() != null) {
                throw future.getCause();
            } else {
                throw new ChannelException(
                        channel,
                        "Failed to connect to server "
                                + getRemoteAddress()
                                + ", the future was not completed within the specified time limit, please check the timeout ["
                                + URLUtils.getTimeout(url) + "] config .");
            }

        } catch (Throwable t) {
            throw new ChannelException(
                    channel,
                    "Failed to connect to server "
                            + getRemoteAddress()
                            + ", the future was not completed within the specified time limit, please check the timeout ["
                            + URLUtils.getTimeout(url) + "] config .");
        } finally {
            if (channel != null && !channel.isConnected()) {
                future.cancel();
            }
        }
    }

    @Override
    protected void doClose() throws ChannelException {
        //anything else?
    }

    @Override
    protected void doDisconnect() throws ChannelException {
        //anything else?
    }

    public Future request(Object request) throws ChannelException {
        return ChannelUtil.request(channel, request);
    }

    public void send(Object message) throws ChannelException {
        channel.send(message);

    }

    public InetSocketAddress getConnectAddress() {
        return new InetSocketAddress(NetUtils.filterLocalHost(url.getHost()), url.getPort());
    }

    public InetSocketAddress getRemoteAddress() {
        if (channel == null)
            return url.toInetSocketAddress();
        return channel.getRemoteAddress();
    }

    private class NettyHandlerAdpater extends SimpleChannelHandler {

        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
                throws Exception {
            handler.connected(channel);
        }

        @Override
        public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e)
                throws Exception {
            handler.disconnected(channel);
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            handler.received(channel, e.getMessage());
        }

        @Override
        public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            super.writeRequested(ctx, e);
            handler.sent(channel, e.getMessage());
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            handler.caught(channel, e.getCause());
        }
    }
}
