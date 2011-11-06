package com.redcreen.rpcplus.codec;

import java.io.InputStream;

import com.redcreen.rpcplus.Annotations.API;
import com.redcreen.rpcplus.Annotations.Prototype;
import com.redcreen.rpcplus.Annotations.ThreadSafe;
import com.redcreen.rpcplus.channel.Channel;

/**
 * data frame 
 */

@API
@Prototype
@ThreadSafe
public class Frame {

    Channel     channel;
    InputStream inputstream;

    /**
     * @param channel
     * @param inputstream
     */
    public Frame(Channel channel, InputStream inputstream){
        super();
        this.channel = channel;
        this.inputstream = inputstream;
    }

    /**
     * @return the channel
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * @return the inputstream
     */
    public InputStream getInputstream() {
        return inputstream;
    }

}
