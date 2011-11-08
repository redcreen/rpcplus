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
package com.redcreen.rpcplus.cluster;

import java.util.List;

import com.redcreen.rpcplus.Invocation;
import com.redcreen.rpcplus.InvokeException;
import com.redcreen.rpcplus.Invoker;
import com.redcreen.rpcplus.support.URL;

/**
 * Directory. (SPI, Singleton, ThreadSafe) <a href="http://en.wikipedia.org/wiki/Directory_service">Directory
 * Service</a>
 */
public interface Directory<T>{
    /**
     * get url.
     * 
     * @return url.
     */
    URL getUrl();
    
    /**
     * get service type.
     * 
     * @return service type.
     */
    Class<T> getInterface();

    /**
     * list invokers.
     * 
     * @return invokers
     */
    List<Invoker<T>> list(Invocation invocation) throws InvokeException;

    /**
     * destroy.
     */
    void destroy();

    /**
     * is available.
     * 
     * @return available.
     */
    boolean isAvailable();

}
