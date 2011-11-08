package com.redcreen.rpcplus.handler.serialize;

import com.redcreen.rpcplus.channel.ChannelHandler;
import com.redcreen.rpcplus.handler.AbstractChannelHandlerWrapper;
import com.redcreen.rpcplus.support.URL;

public abstract class AbstractSerializeChannelHandlerWrapper extends AbstractChannelHandlerWrapper {

    public AbstractSerializeChannelHandlerWrapper(URL url, ChannelHandler handler) {
        super(url, handler);
    }
}
