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
package com.redcreen.rpcplus.serialize.fastjson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.redcreen.rpcplus.serialize.ObjectInput;
import com.redcreen.rpcplus.serialize.ObjectOutput;
import com.redcreen.rpcplus.serialize.Serialization;
import com.redcreen.rpcplus.support.Extension;
import com.redcreen.rpcplus.support.URL;

/**
 * FastJsonSerialization
 * 
 */
@Extension("fastjson")
public class FastJsonSerialization implements Serialization {

    public byte getContentTypeId() {
        return 6;
    }

    public String getContentType() {
        return "text/json";
    }
    
    public ObjectOutput serialize(URL url, OutputStream output) throws IOException {
        return new FastJsonObjectOutput(output);
    }

    public ObjectInput deserialize(URL url, InputStream input) throws IOException {
        return new FastJsonObjectInput(input);
    }

}