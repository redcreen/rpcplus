/*
 * Copyright 2011 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.redcreen.rpcplus.cluster.support;

import com.redcreen.rpcplus.Invocation;
import com.redcreen.rpcplus.InvokeException;
import com.redcreen.rpcplus.Invoker;
import com.redcreen.rpcplus.Result;
import com.redcreen.rpcplus.cluster.ClusterInvoker;
import com.redcreen.rpcplus.cluster.Directory;
import com.redcreen.rpcplus.support.URL;

public class AbstractClusterInvoker<T> implements ClusterInvoker<T> {

    protected final Directory<T> directory;
    private volatile boolean     destroyed = false;

    public AbstractClusterInvoker(Directory<T> directory){
        super();
        this.directory = directory;
    }

    public Class<T> getInterface() {
        return directory.getInterface();
    }

    public URL getUrl() {
        return directory.getUrl();
    }

    public boolean isAvailable() {
        if (destroyed == true) {
            return false;
        } else {
            return directory.isAvailable();
        }
    }
    public Result invoke(Invocation invocation) throws InvokeException {
        Invoker<T> invoker = select();
        //TODO
        //directory get list
        //route list
        //loadbalance //TODO loadbalance 状态如何分离？
        //reselect select 支持策略 
        //available check是否应该放入到内置的router上?
        //sticky 放入内置的router上
        return invoker.invoke(invocation);
    }

    protected Invoker<T> select() {
        //get  list
        //route 
        //loadbalance
        return null;
    }
    
    public void destroy() {
        if (destroyed == false){
            directory.destroy();
            destroyed = true;
        }
    }

    public Directory<T> getDirectory() {
        return directory;
    }

}
