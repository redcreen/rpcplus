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
package com.redcreen.rpcplus.codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.redcreen.rpcplus.Annotations.SPI;
import com.redcreen.rpcplus.Annotations.Singleton;
import com.redcreen.rpcplus.Annotations.ThreadSafe;
import com.redcreen.rpcplus.channel.Channel;

/**
 * Codec. 
 * 
 */
@SPI
@Singleton
@ThreadSafe
public interface Codec {

    /**
     * Encode message.
     * 
     * @param channel channel.
     * @param output output stream.
     * @param message message.
     */
    void encode(Channel channel, OutputStream output, Object message) throws IOException;

	/**
	 * Decode message.
	 * 
	 * @see #NEED_MORE_INPUT
	 * @param channel channel.
	 * @param input input stream.
	 * @return InputStream or null.
	 */
	Frame decode(Channel channel, InputStream input) throws IOException;

}