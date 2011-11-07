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

import com.redcreen.rpcplus.annotations.SPI;
import com.redcreen.rpcplus.annotations.Singleton;
import com.redcreen.rpcplus.annotations.ThreadSafe;


/**
 * Filter.
 * 
 */
@SPI
@Singleton
@ThreadSafe
public interface Filter {

	/**
	 * do invoke filter.
	 * 
	 * <code>
     *     return invoker.invoke(invocation);
     * </code>
     * 
	 * @param invoker service
	 * @param invocation invocation.
	 * @return invoke result.
	 * @throws RpcException
	 */
	Result invoke(Invoker<?> invoker, Invocation invocation) throws InvokeException;

}