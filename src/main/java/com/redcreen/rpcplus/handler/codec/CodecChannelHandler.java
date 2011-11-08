package com.redcreen.rpcplus.handler.codec;

import java.io.IOException;
import java.io.InputStream;

import com.redcreen.rpcplus.channel.Channel;
import com.redcreen.rpcplus.channel.ChannelException;
import com.redcreen.rpcplus.channel.ChannelHandler;
import com.redcreen.rpcplus.codec.Codec;
import com.redcreen.rpcplus.codec.object.ExchangeCodec;
import com.redcreen.rpcplus.handler.AbstractChannelHandlerWrapper;
import com.redcreen.rpcplus.support.ExtensionLoader;
import com.redcreen.rpcplus.support.URL;

public class CodecChannelHandler extends AbstractChannelHandlerWrapper{
    private final Codec exchangeCodec = getCodec(ExchangeCodec.NAME);
    /**
     * @param url
     * @param handler
     */
    public CodecChannelHandler(URL url, ChannelHandler handler) {
        super(handler);
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
