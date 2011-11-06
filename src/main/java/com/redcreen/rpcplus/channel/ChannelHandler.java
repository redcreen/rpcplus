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

import com.redcreen.rpcplus.Annotations.API;
import com.redcreen.rpcplus.Annotations.Prototype;
import com.redcreen.rpcplus.Annotations.ThreadSafe;

/**
 * ChannelHandler. 
 */
@API
@Prototype
@ThreadSafe
public interface ChannelHandler {

    /**
     * on channel connected.
     * 
     * @param channel channel.
     */
    void connected(Attributeable channel) throws ChannelException;

    /**
     * on channel disconnected.
     * 
     * @param channel channel.
     */
    void disconnected(Attributeable channel) throws ChannelException;

    /**
     * on message sent.
     * 
     * @param channel channel.
     * @param message message.
     */
    void sent(Attributeable channel, Object message) throws ChannelException;

    /**
     * on message received.
     * 
     * @param channel channel.
     * @param message message.
     */
    void received(Attributeable channel, Object message) throws ChannelException;

    /**
     * on exception caught.
     * 
     * @param channel channel.
     * @param exception exception.
     */
    void caught(Attributeable channel, Throwable exception) throws ChannelException;

}