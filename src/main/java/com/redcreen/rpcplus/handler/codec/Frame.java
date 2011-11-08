package com.redcreen.rpcplus.handler.codec;

import java.io.InputStream;

import com.redcreen.rpcplus.channel.Channel;
import com.redcreen.rpcplus.support.API;
import com.redcreen.rpcplus.support.Prototype;
import com.redcreen.rpcplus.support.ThreadSafe;

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
