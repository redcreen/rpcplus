package com.redcreen.rpcplus.channel.adapter.netty;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.internal.ConcurrentHashMap;

import com.redcreen.rpcplus.channel.ChannelException;
import com.redcreen.rpcplus.channel.ChannelHandler;
import com.redcreen.rpcplus.channel.adapter.Server;
import com.redcreen.rpcplus.support.URL;
import com.redcreen.rpcplus.util.NamedThreadFactory;
import com.redcreen.rpcplus.util.URLUtils;
import com.redcreen.rpcplus.util.io.UnsafeByteArrayInputStream;

public class NettyServer extends Server {
    private ServerBootstrap                                                    bootstrap;
    private Channel                                                            channel;
    private final ConcurrentMap<Channel, com.redcreen.rpcplus.channel.Channel> channels = new ConcurrentHashMap<Channel, com.redcreen.rpcplus.channel.Channel>();

    public NettyServer(URL url, ChannelHandler handler) throws ChannelException {
        super(url, handler);
    }

    @Override
    protected void doOpen() throws Throwable {
        ExecutorService boss = Executors.newCachedThreadPool(new NamedThreadFactory(
                "NettyServerBoss", true));
        ExecutorService worker = Executors.newCachedThreadPool(new NamedThreadFactory(
                "NettyServerWorker", true));
        ChannelFactory channelFactory = new NioServerSocketChannelFactory(boss, worker,
                URLUtils.getIoThreads(url));
        bootstrap = new ServerBootstrap(channelFactory);

        final NettyHandlerAdpater handlerAdapater = new NettyHandlerAdpater();
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("handlerAdapter", handlerAdapater);
                return pipeline;
            }
        });
        // bind
        channel = bootstrap.bind(getBindAddress());
    }

    @Override
    protected void doClose() throws Throwable {
        for (com.redcreen.rpcplus.channel.Channel channel : channels.values()) {
            channel.close(URLUtils.getCloseTimeout(url));
        }
        channel.close().await(URLUtils.getCloseTimeout(url));
    }

    private class NettyHandlerAdpater extends SimpleChannelHandler {

        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
                throws Exception {
            handler.connected(getOrAddChannel(ctx.getChannel()));
        }

        @Override
        public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e)
                throws Exception {
            handler.disconnected(getOrAddChannel(ctx.getChannel()));
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
            handler.received(getOrAddChannel(ctx.getChannel()), bis);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            handler.caught(getOrAddChannel(ctx.getChannel()), e.getCause());
        }

        private com.redcreen.rpcplus.channel.Channel getOrAddChannel(Channel nettyChannel) {
            com.redcreen.rpcplus.channel.Channel channel = channels.get(nettyChannel);
            if (channel == null) {
                com.redcreen.rpcplus.channel.Channel newChannel = new NettyChannelAdpater(
                        nettyChannel, handler, url);
                com.redcreen.rpcplus.channel.Channel ret = channels.putIfAbsent(nettyChannel,
                        newChannel);
                if (ret == null) {
                    channel = newChannel;
                } else {
                    channel = ret;
                }
            }
            return channel;
        }
    }
}
