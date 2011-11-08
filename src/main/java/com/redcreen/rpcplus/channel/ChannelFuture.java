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
package com.redcreen.rpcplus.channel;


/**
 * ChannelFuture. (API/SPI, Prototype, ThreadSafe)
 */
public interface ChannelFuture {

    /**
     * get result.
     * 
     * @return result.
     */
    Object get() throws ChannelException;

    /**
     * get result with the specified timeout.
     * 
     * @param timeoutInMillis timeout.
     * @return result.
     */
    Object get(int timeoutInMillis) throws ChannelException;

    /**
     * check is done.
     * 
     * @return done or not.
     */
    boolean isDone();

    /**
     * set callback.
     * 
     * @param callback
     */
    void setCallback(Callback callback);

    public static interface Callback {

        /**
         * done.
         * 
         * @param response
         */
        void done(Object response);

        /**
         * caught exception.
         * 
         * @param exception
         */
        void caught(Throwable exception);

    }

}