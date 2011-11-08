package com.redcreen.rpcplus.channel.adapter.netty;

import java.net.InetSocketAddress;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;

import com.redcreen.rpcplus.channel.ChannelException;
import com.redcreen.rpcplus.channel.ChannelHandler;
import com.redcreen.rpcplus.channel.Future;
import com.redcreen.rpcplus.channel.support.Attributes;
import com.redcreen.rpcplus.channel.support.ChannelUtil;
import com.redcreen.rpcplus.support.URL;
import com.redcreen.rpcplus.util.URLUtils;

public class NettyChannelAdpater implements com.redcreen.rpcplus.channel.Channel {
    private final URL            url;
    private final ChannelHandler handler;

    private Channel              nettyChannel;
    private Attributes           attributes = new Attributes();

    public NettyChannelAdpater(Channel nettyChannel, ChannelHandler handler, URL url) {
        super();
        this.nettyChannel = nettyChannel;
        this.handler = handler;
        this.url = url;
    }

    public boolean hasAttribute(String key) {
        return attributes.hasAttribute(key);
    }

    public Object getAttribute(String key) {
        return attributes.getAttribute(key);
    }

    public void setAttribute(String key, Object value) {
        attributes.setAttribute(key, value);
    }

    public void removeAttribute(String key) {
        attributes.removeAttribute(key);
    }

    public void send(Object message) throws ChannelException {
        send(message, URLUtils.getSent(url));
    }

    public void send(Object message, boolean sent) throws ChannelException {
        boolean success = true;
        try {
            ChannelFuture future = nettyChannel.write(message);
            if (sent) {
                success = future.await(URLUtils.getTimeout(url));
            }
        } catch (Throwable e) {
            throw new ChannelException(e);
        }

        if (!success) {
            throw new ChannelException(new IllegalStateException("channel wirte message error"));
        }
    }
    
    @Override
    public Future request(Object request) throws ChannelException {
        return ChannelUtil.request(this, request);
    }

    public void close(int timeout) throws ChannelException {
        try {
            Channel channelCopy = nettyChannel;
            if (channelCopy != null) {
                channelCopy.close().await(URLUtils.getCloseTimeout(url));
            }
        } catch (Throwable t) {

        }
        attributes.clear();
    }

    public boolean isConnected() {
        Channel channelCopy = nettyChannel;
        return channelCopy == null ? false : channelCopy.isConnected();
    }

    public boolean isClosed() {
        Channel channelCopy = nettyChannel;
        return channelCopy == null ? false : !channelCopy.isOpen();
    }

    public URL getUrl() {
        return url;
    }

    public ChannelHandler getChannelHandler() {
        return handler;
    }

    public InetSocketAddress getLocalAddress() {
        Channel channelCopy = nettyChannel;
        return channelCopy == null ? null : (InetSocketAddress) channelCopy.getLocalAddress();
    }

    public InetSocketAddress getRemoteAddress() {
        Channel channelCopy = nettyChannel;
        return channelCopy == null ? null : (InetSocketAddress) channelCopy.getRemoteAddress();
    }
}
