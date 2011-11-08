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

import java.net.InetSocketAddress;

import com.redcreen.rpcplus.support.URL;

/**
 * Channel. (API/SPI, Prototype, ThreadSafe)
 */
public interface Channel extends Attributeable {

    /**
     * send message.
     * 
     * @param message
     * @throws ChannelException
     */
    void send(Object message) throws ChannelException;

    /**
     * send message.
     * 
     * @param message
     * @param sent 是否已发送完成
     */
    void send(Object message, boolean sent) throws ChannelException;
    
    Future request(Object request) throws ChannelException;

    /**
     * Graceful close the channel.
     */
    void close(int timeout) throws ChannelException;

    /**
     * is connected.
     * 
     * @return connected
     */
    boolean isConnected();

    /**
     * is closed.
     * 
     * @return closed
     */
    boolean isClosed();

    /**
     * get url.
     * 
     * @return url
     */
    URL getUrl();

    /**
     * get channel handler.
     * 
     * @return channel handler
     */
    ChannelHandler getChannelHandler();

    /**
     * get local address.
     * 
     * @return local address.
     */
    InetSocketAddress getLocalAddress();

    /**
     * get remote address.
     * 
     * @return remote address.
     */
    InetSocketAddress getRemoteAddress();

    

}
