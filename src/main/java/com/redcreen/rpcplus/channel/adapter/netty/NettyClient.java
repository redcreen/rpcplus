package com.redcreen.rpcplus.channel.adapter.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.redcreen.rpcplus.channel.ChannelException;
import com.redcreen.rpcplus.channel.adapter.AbstractClient;
import com.redcreen.rpcplus.channel.support.Response;
import com.redcreen.rpcplus.codec.Codec;
import com.redcreen.rpcplus.support.Constants.ChannelConstants;
import com.redcreen.rpcplus.support.Extension;
import com.redcreen.rpcplus.support.ExtensionLoader;
import com.redcreen.rpcplus.util.ExecutorUtils;
import com.redcreen.rpcplus.util.URLUtils;
import com.redcreen.rpcplus.util.io.UnsafeByteArrayInputStream;
import com.redcreen.rpcplus.util.io.UnsafeByteArrayOutputStream;

@Extension("nettyclient")
public class NettyClient extends AbstractClient {

    private static final ChannelFactory channelFactory = initChannelFactory();
    private ClientBootstrap             bootstrap;

    private static ChannelFactory initChannelFactory() {
        return new NioClientSocketChannelFactory(
                ExecutorUtils.newCachedExecutor("NettyClientBoss"),
                ExecutorUtils.newCachedExecutor("NettyClientWorker"),
                ChannelConstants.IO_THREADS_DEFAULT);
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
        final InternalEncoder encoder = new InternalEncoder();
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            public ChannelPipeline getPipeline() {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("handler", nettyHandler);
                pipeline.addLast("encoder", encoder);
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

    public void send(Object message) throws ChannelException {
        channel.send(message);

    }

    public InetSocketAddress getConnectAddress() {
        return new InetSocketAddress(url.getHost(), url.getPort());
    }

    public InetSocketAddress getRemoteAddress() {
        if (channel == null)
            return url.toInetSocketAddress();
        return channel.getRemoteAddress();
    }

    @Sharable
    private class InternalEncoder extends OneToOneEncoder {
        Codec codec = ExtensionLoader.getExtensionLoader(Codec.class).getExtension(URLUtils.getCodec(url));
        @Override
        protected Object encode(ChannelHandlerContext ctx, Channel ch, Object msg) throws Exception {
            UnsafeByteArrayOutputStream os = new UnsafeByteArrayOutputStream(1024); // 不需要关闭
            if(!(msg instanceof Response)){
                codec.encode(channel, os, msg);
            }else {
                codec.encode(channel, os, msg);
            }
            return ChannelBuffers.wrappedBuffer(os.toByteBuffer());
        }
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
            Object o = e.getMessage();
            if (!(o instanceof ChannelBuffer)) {
                ctx.sendUpstream(e);
                return;
            }

            ChannelBuffer input = (ChannelBuffer) o;
            int readable = input.readableBytes();
            if (readable <= 0) {
                return;
            }
            //TODO must copy?
            UnsafeByteArrayInputStream bis = new UnsafeByteArrayInputStream(input.array());
            handler.received(channel, bis);
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
