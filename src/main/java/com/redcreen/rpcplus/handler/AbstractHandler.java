package com.redcreen.rpcplus.handler;

import com.redcreen.rpcplus.channel.Channel;
import com.redcreen.rpcplus.channel.ChannelException;
import com.redcreen.rpcplus.channel.ChannelHandler;

public abstract class AbstractHandler implements ChannelHandler {

    @Override
    public void connected(Channel channel) throws ChannelException {
        //do nothing
    }

    @Override
    public void disconnected(Channel channel) throws ChannelException {
        //do nothing
    }

    @Override
    public void sent(Channel channel, Object message) throws ChannelException {
        //do nothing
    }

    @Override
    public void received(Channel channel, Object message) throws ChannelException {
        //do nothing
    }

    @Override
    public void caught(Channel channel, Throwable exception) throws ChannelException {
        //do nothing
    }
}
