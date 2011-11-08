/*
 * Copyright 2011 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.redcreen.rpcplus.channel.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.redcreen.rpcplus.channel.Attributeable;

public class Attributes implements Attributeable {

    private final Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();

    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void setAttribute(String key, Object value) {
        if (value == null) { 
            attributes.remove(key);
        } else {
            attributes.put(key, value);
        }
    }

    public void removeAttribute(String key) {
        attributes.remove(key);
    }
    
    public void clear(){
        attributes.clear();
    }

}
