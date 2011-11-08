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
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redcreen.rpcplus.channel.Channel;
import com.redcreen.rpcplus.serialize.Serialization;
import com.redcreen.rpcplus.support.ExtensionLoader;
import com.redcreen.rpcplus.util.URLUtils;

public abstract class AbstractCodec implements Codec {

    protected static Map<Byte, Serialization> ID_SERIALIZATION_MAP = new HashMap<Byte, Serialization>();

    private static final Logger               logger               = LoggerFactory
                                                                           .getLogger(AbstractCodec.class);

    static {
        Set<String> supportedExtensions = ExtensionLoader.getExtensionLoader(Serialization.class)
                .getSupportedExtensions();
        for (String name : supportedExtensions) {
            Serialization serialization = ExtensionLoader.getExtensionLoader(Serialization.class)
                    .getExtension(name);
            byte idByte = serialization.getContentTypeId();
            if (ID_SERIALIZATION_MAP.containsKey(idByte)) {
                logger.error("Serialization extension " + serialization.getClass().getName()
                        + " has duplicate id to Serialization extension "
                        + ID_SERIALIZATION_MAP.get(idByte).getClass().getName()
                        + ", ignore this Serialization extension");
                continue;
            }
            ID_SERIALIZATION_MAP.put(idByte, serialization);
        }
    }
    
    protected static final Serialization getSerializationById(Byte id) {
        return ID_SERIALIZATION_MAP.get(id);
    }

    protected Serialization getSerialization(Channel channel) {
        Serialization serialization = ExtensionLoader.getExtensionLoader(Serialization.class)
                .getExtension(URLUtils.getSerialization(channel.getUrl()));
        return serialization;
    }

    protected void checkPayload(Channel channel, long size) throws IOException {

        int payload = URLUtils.getPayload(channel.getUrl());
        if (size > payload) {
            throw new IOException("Data length too large: " + size + ", max payload: " + payload
                    + ", channel: " + channel);
        }
    }

    @Override
    public Object read(Channel channel, InputStream input) throws IOException {
        throw new UnsupportedOperationException();
    }
}
