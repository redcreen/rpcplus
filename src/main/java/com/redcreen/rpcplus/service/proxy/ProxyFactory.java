/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redcreen.rpcplus.service.proxy;

import com.redcreen.rpcplus.service.InvokeException;
import com.redcreen.rpcplus.service.Invoker;
import com.redcreen.rpcplus.support.URL;

/**
 * ProxyFactory. (SPI, Singleton, ThreadSafe)
 * 
 */
public interface ProxyFactory {

    /**
     * create proxy.
     * 
     * @param invoker
     * @param types
     * @return proxy
     */
    <T> T getProxy(Invoker<T> invoker, Class<?>... types) throws InvokeException;

    /**
     * create invoker.
     * 
     * @param <T>
     * @param proxy
     * @param type
     * @param url
     * @return invoker
     */
    <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) throws InvokeException;

}