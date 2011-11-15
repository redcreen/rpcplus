package com.redcreen.rpcplus.handler.codec;

import com.redcreen.rpcplus.channel.Channel;
import com.redcreen.rpcplus.channel.ChannelException;
import com.redcreen.rpcplus.channel.DownStreamChannelHandler;
import com.redcreen.rpcplus.channel.support.Response;
import com.redcreen.rpcplus.support.ExtensionLoader;
import com.redcreen.rpcplus.support.URLUtils;
import com.redcreen.rpcplus.util.io.UnsafeByteArrayOutputStream;

public class EncodeHandlerWrapper implements DownStreamChannelHandler {
    
    DownStreamChannelHandler handler ;
    
    public EncodeHandlerWrapper(DownStreamChannelHandler handler) {
        super();
        this.handler = handler;
    }

    @Override
    public void write(Channel channel, Object msg) throws ChannelException {
        Codec codec = ExtensionLoader.getExtensionLoader(Codec.class).getExtension(
                URLUtils.getCodec(channel.getUrl()));
        UnsafeByteArrayOutputStream os = new UnsafeByteArrayOutputStream(1024); 
        try {
            if (!(msg instanceof Response)) {
                codec.encode(channel, os, msg);
            } else {
                codec.encode(channel, os, msg);
            }
        } catch (Throwable t) {
            throw new ChannelException(t);
        }
        handler.write(channel, os.toByteBuffer());
        //        return ChannelBuffers.wrappedBuffer(os.toByteBuffer());
    }

}
