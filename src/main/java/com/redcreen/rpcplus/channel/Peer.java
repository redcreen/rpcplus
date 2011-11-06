/*
 * Copyright 2011 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.redcreen.rpcplus.channel;

import com.redcreen.rpcplus.support.URL;

public interface Peer {

    public abstract Channel getChannel();

    public abstract URL getURL();
    
    public void send(Object message) throws ChannelException;
    
    public Future request(Object message)  throws ChannelException;

    public abstract void close(int timeout) throws ChannelException;

}