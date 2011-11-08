package com.redcreen.rpcplus.handler.codec;

import java.io.IOException;
import java.io.InputStream;

import com.redcreen.rpcplus.channel.Channel;
import com.redcreen.rpcplus.channel.ChannelException;
import com.redcreen.rpcplus.channel.ChannelHandler;
import com.redcreen.rpcplus.handler.AbstractHandlerWrapper;
import com.redcreen.rpcplus.handler.codec.object.ExchangeCodec;
import com.redcreen.rpcplus.support.ExtensionLoader;
import com.redcreen.rpcplus.support.URL;

public class CodecHandlerWrapper extends AbstractHandlerWrapper{
    private final Codec exchangeCodec = getCodec(ExchangeCodec.NAME);
    /**
     * @param url
     * @param handler
     */
    public CodecHandlerWrapper(URL url, ChannelHandler handler) {
        super(url, handler);
    }
    
    public void received(Channel channel, Object message) throws ChannelException {
        if (!(message instanceof InputStream)) {
            handler.received(channel, message);
            return ;
        }
        try{
            Object decodedObject = decode(channel, (InputStream)message);
            handler.received(channel, decodedObject);
        }catch (IOException e) {
            throw new ChannelException(channel,e);
        }
    }

    public Object decode(Channel channel, InputStream is) throws IOException{
        if (exchangeCodec.recognize(is)){
            return exchangeCodec.decode(channel, is);
        } 
        throw new IOException("unrecognized data");
    }
    
    private Codec getCodec(String name){
        return ExtensionLoader.getExtensionLoader(Codec.class).getExtension(name);
    }
}
