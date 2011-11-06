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
package com.redcreen.rpcplus.protocol;

import com.redcreen.rpcplus.InvokeException;
import com.redcreen.rpcplus.Invoker;
import com.redcreen.rpcplus.support.URL;

/**
 * Protocol. (SPI, Singleton, ThreadSafe)
 */
public interface Protocol {

    <T> void export(Invoker<T> invoker) throws InvokeException;

    <T> void unexport(Invoker<T> invoker) throws InvokeException;

    <T> Invoker<T> refer(Class<T> type, URL url) throws InvokeException;

    <T> void unrefer(Class<T> type, URL url) throws InvokeException;

    void destroy();
}
