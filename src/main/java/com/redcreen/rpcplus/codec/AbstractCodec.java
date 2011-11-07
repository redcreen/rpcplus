/**
 * Project: rpcplus
 * 
 * File Created at 2011-11-7
 * $Id$
 * 
 * Copyright 1999-2100 Alibaba.com Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Alibaba Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Alibaba.com.
 */
package com.redcreen.rpcplus.codec;

import java.io.IOException;

import com.redcreen.rpcplus.channel.Channel;
import com.redcreen.rpcplus.util.URLUtils;



public abstract class AbstractCodec implements Codec {
    protected void checkPayload(Channel channel, long size) throws IOException {
        
        int payload = URLUtils.getPayload(channel.getUrl());
        if (size > payload) {
            throw new IOException("Data length too large: " + size + ", max payload: " + payload + ", channel: " + channel);
        }
    }
}
