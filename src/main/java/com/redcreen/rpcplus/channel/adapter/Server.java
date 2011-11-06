/*
 * Copyright 2011 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.redcreen.rpcplus.channel.adapter;

import com.redcreen.rpcplus.channel.Channel;
import com.redcreen.rpcplus.channel.ChannelException;
import com.redcreen.rpcplus.channel.Peer;
import com.redcreen.rpcplus.support.URL;

public abstract class Server implements Peer {

    public Channel getChannel() {
        // TODO Auto-generated method stub
        return null;
    }

    public URL getURL() {
        // TODO Auto-generated method stub
        return null;
    }

    public void close(int timeout) throws ChannelException {
        // TODO Auto-generated method stub

    }
}
