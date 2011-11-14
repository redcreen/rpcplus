package com.redcreen.rpcplus.handler.portunification;

import java.io.IOException;
import java.io.InputStream;

import com.redcreen.rpcplus.channel.Channel;
import com.redcreen.rpcplus.channel.ChannelHandler;
import com.redcreen.rpcplus.handler.codec.Codec;
import com.redcreen.rpcplus.handler.codec.frame.ExchangeCodec;
import com.redcreen.rpcplus.support.ExtensionLoader;
import com.redcreen.rpcplus.support.URL;

public class PortunificationHandlerWrapper extends AbstractFrameCodecHandlerWrapper{
    private final Codec exchangeCodec = getCodec(ExchangeCodec.NAME);
    private final Codec telnetCodec = getCodec("telnet");
    /**
     * @param url
     * @param handler
     */
    public PortunificationHandlerWrapper(URL url, ChannelHandler handler) {
        super(url, handler);
        
    }

    @Override
    public Object decode(Channel channel, InputStream is) throws IOException{
        if (exchangeCodec.recognize(is)){
            return exchangeCodec.read(channel, is);
        } else if (telnetCodec.recognize(is)){
            return telnetCodec.read(channel, is);
        } 
        return null;
    }
    
    private Codec getCodec(String name){
        return ExtensionLoader.getExtensionLoader(Codec.class).getExtension(name);
    }
}
