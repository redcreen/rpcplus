/*
 * Copyright 2011 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.redcreen.rpcplus.channel.support;

import com.redcreen.rpcplus.channel.Channel;
import com.redcreen.rpcplus.channel.ChannelException;
import com.redcreen.rpcplus.channel.ChannelFuture;
import com.redcreen.rpcplus.support.Constants;
import com.redcreen.rpcplus.support.URLUtils;

public class ChannelUtil {
    public static ChannelFuture request(Channel channel, Object message) throws ChannelException {
        // create request.
        Request req = new Request();
        req.setVersion(Constants.VERSION_VALUE);
        req.setTwoWay(true);
        req.setData(message);
        DefaultFuture future = new DefaultFuture(channel, req, URLUtils.getTimeout(channel.getUrl()));
        try{
            channel.send(req);
        }catch (ChannelException e) {
            future.cancel();
            throw e;
        }
        return future;
    }
}
