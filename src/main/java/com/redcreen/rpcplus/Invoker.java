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
package com.redcreen.rpcplus;

import com.redcreen.rpcplus.support.API;
import com.redcreen.rpcplus.support.Prototype;
import com.redcreen.rpcplus.support.SPI;
import com.redcreen.rpcplus.support.ThreadSafe;
import com.redcreen.rpcplus.support.URL;


/**
 * Invoker. 
 */
@SPI
@API
@Prototype
@ThreadSafe
public interface Invoker<T> {

    /**
     * get service interface.
     * 
     * @return service interface.
     */
    Class<T> getInterface();

    /**
     * get service url.
     * 
     * @return service url.
     */
    URL getUrl();

    /**
     * is available.
     * 
     * @return available.
     */
    boolean isAvailable();

    /**
     * invoke.
     * 
     * @param invocation
     * @return result
     * @throws InvokeException
     */
    Result invoke(Invocation invocation) throws InvokeException;

    /**
     * destroy.
     */
    void destroy();

}
