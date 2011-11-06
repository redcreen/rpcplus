/*
 * Copyright 2011 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.redcreen.rpcplus.channel;

public interface Attributeable {

    /**
     * has attribute.
     * 
     * @param key key.
     * @return has or has not.
     */
    public abstract boolean hasAttribute(String key);

    /**
     * get attribute.
     * 
     * @param key key.
     * @return value.
     */
    public abstract Object getAttribute(String key);

    /**
     * set attribute.
     * 
     * @param key key.
     * @param value value.
     */
    public abstract void setAttribute(String key, Object value);

    /**
     * remove attribute.
     * 
     * @param key key.
     */
    public abstract void removeAttribute(String key);

}
