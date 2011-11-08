package com.redcreen.rpcplus.handler.codec;

import java.io.IOException;
import java.io.InputStream;

import com.redcreen.rpcplus.channel.Channel;
import com.redcreen.rpcplus.channel.ChannelHandler;
import com.redcreen.rpcplus.codec.Codec;
import com.redcreen.rpcplus.codec.object.ExchangeCodec;
import com.redcreen.rpcplus.support.ExtensionLoader;
import com.redcreen.rpcplus.support.URL;

public class PortunificationHandler extends AbstractFrameCodecHandlerWrapper{
    private final Codec exchangeCodec = getCodec(ExchangeCodec.NAME);
    private final Codec telnetCodec = getCodec("telnet");
    /**
     * @param url
     * @param handler
     */
    public PortunificationHandler(URL url, ChannelHandler handler) {
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
