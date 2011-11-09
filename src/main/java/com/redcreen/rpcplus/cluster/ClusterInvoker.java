/*
 * Copyright 2011 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.redcreen.rpcplus.cluster;

import com.redcreen.rpcplus.service.Invoker;

/**
 */
public interface ClusterInvoker<T> extends Invoker<T> {

    public Directory<T> getDirectory();
    
}
