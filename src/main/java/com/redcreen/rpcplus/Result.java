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

import com.redcreen.rpcplus.Annotations.API;
import com.redcreen.rpcplus.Annotations.Prototype;
import com.redcreen.rpcplus.Annotations.SPI;
import com.redcreen.rpcplus.Annotations.ThreadSafe;

/**
 * RPC invoke result.
 * 
 * @serial Don't change the class name.
 */
@SPI
@API
@Prototype
@ThreadSafe
public interface Result {

    /**
     * Has exception.
     * 
     * @return has exception.
     */
    boolean hasException();

	/**
	 * Get invoke result.
	 * 
	 * @return result if has exception throw it.
	 * @throws Throwable.
	 */
	Object getResult();

	/**
	 * Get exception.
	 * 
	 * @return exception if no exception return null.
	 */
	Throwable getException();

    /**
     * Recreate.
     * 
     * <code>
     * if (hasException()) {
     *     throw getException();
     * } else {
     *     return getResult();
     * }
     * </code>
     */
    Object recreate() throws Throwable;

}