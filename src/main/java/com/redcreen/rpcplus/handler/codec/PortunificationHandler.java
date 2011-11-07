package com.redcreen.rpcplus.handler.codec;

import java.io.IOException;
import java.io.InputStream;

import com.redcreen.rpcplus.channel.Channel;
import com.redcreen.rpcplus.channel.ChannelHandler;
import com.redcreen.rpcplus.codec.Codec;
import com.redcreen.rpcplus.codec.Frame;
import com.redcreen.rpcplus.support.Constants.ChannelConstants;
import com.redcreen.rpcplus.support.ServiceContext;
import com.redcreen.rpcplus.support.URL;

public class PortunificationHandler extends AbstractCodecHandlerWrapper{
    private final Codec codec ;
    
    /**
     * @param url
     * @param handler
     */
    public PortunificationHandler(URL url, ChannelHandler handler) {
        super(url, handler);
        codec = ServiceContext.getExtensionLoader(Codec.class).getExtension(
                url.getParameter(ChannelConstants.CODEC_KEY, ChannelConstants.CODEC_DEFAULT));
    }

    @Override
    public Object decode(Channel channel, InputStream is) throws IOException{
        Frame frame = codec.decode(channel, is);
        return frame;
    }
}
