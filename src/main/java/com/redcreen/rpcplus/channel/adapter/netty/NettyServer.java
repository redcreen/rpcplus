package com.redcreen.rpcplus.channel.adapter.netty;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.jboss.netty.util.internal.ConcurrentHashMap;

import com.redcreen.rpcplus.channel.adapter.AbstractServer;
import com.redcreen.rpcplus.channel.support.Response;
import com.redcreen.rpcplus.codec.Codec;
import com.redcreen.rpcplus.support.Extension;
import com.redcreen.rpcplus.support.ExtensionLoader;
import com.redcreen.rpcplus.util.NamedThreadFactory;
import com.redcreen.rpcplus.util.URLUtils;
import com.redcreen.rpcplus.util.io.UnsafeByteArrayInputStream;
import com.redcreen.rpcplus.util.io.UnsafeByteArrayOutputStream;

@Extension("nettyserver")
public class NettyServer extends AbstractServer {
    private ServerBootstrap                                                    bootstrap;
    private Channel                                                            channel;
    private final ConcurrentMap<Channel, com.redcreen.rpcplus.channel.Channel> channels = new ConcurrentHashMap<Channel, com.redcreen.rpcplus.channel.Channel>();

    @Override
    public Collection<com.redcreen.rpcplus.channel.Channel> getChannels() {
        return channels.values();
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
        final InternalEncoder encoder = new InternalEncoder();
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("handlerAdapter", handlerAdapater);
                //TODO encode put channel handler ?
                pipeline.addLast("encoder", encoder);
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
    
    @Sharable
    private class InternalEncoder extends OneToOneEncoder {
        Codec codec = ExtensionLoader.getExtensionLoader(Codec.class).getExtension(URLUtils.getCodec(url));
        @Override
        protected Object encode(ChannelHandlerContext ctx, Channel ch, Object msg) throws Exception {
            //TODO codec 自动识别.
            UnsafeByteArrayOutputStream os = new UnsafeByteArrayOutputStream(1024); // 不需要关闭
            if(!(msg instanceof Response)){
                codec.encode(getChannel(ctx.getChannel()), os, msg);
            }else {
                codec.encode(getChannel(ctx.getChannel()), os, msg);
            }
            return ChannelBuffers.wrappedBuffer(os.toByteBuffer());
        }
    }
    private class NettyHandlerAdpater extends SimpleChannelHandler {

        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
                throws Exception {
            handler.connected(addChannel(ctx.getChannel()));
        }

        @Override
        public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e)
                throws Exception {
            handler.disconnected(channels.remove(ctx.getChannel()));
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
            handler.received(getChannel(ctx.getChannel()), bis);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            handler.caught(getChannel(ctx.getChannel()), e.getCause());
        }
        
        @Override
        public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            super.writeRequested(ctx, e);
            handler.sent(getChannel(ctx.getChannel()), e.getMessage());
        }
    }
    private com.redcreen.rpcplus.channel.Channel addChannel(Channel nettyChannel) {
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
    private com.redcreen.rpcplus.channel.Channel getChannel(Channel nettyChannel) {
        return channels.get(nettyChannel);
    }
}
