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
package com.redcreen.rpcplus.service.invocationSerialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.redcreen.rpcplus.handler.codec.serialize.ObjectInput;
import com.redcreen.rpcplus.handler.codec.serialize.ObjectOutput;
import com.redcreen.rpcplus.handler.codec.serialize.Serialization;
import com.redcreen.rpcplus.handler.codec.serialize.SerializationWrapper;
import com.redcreen.rpcplus.support.URL;

/**
 * InvocationSerialization
 * 
 */
public class InvocatinoSerializationWrapper implements Serialization,SerializationWrapper {
    
    private final Serialization serialization;
    
    public InvocatinoSerializationWrapper(Serialization serialization) {
        super();
        this.serialization = serialization;
    }

    public byte getContentTypeId() {
        return -1; //no used
    }

    public String getContentType() {
        return "x-application/invocation";
    }
    
    public ObjectOutput serialize(URL url, OutputStream os) throws IOException {
        ObjectOutput out = serialization.serialize(url, os);
        return new InvocationObjectOutput(out);
    }

    public ObjectInput deserialize(URL url, InputStream is) throws IOException {
        ObjectInput input = serialization.deserialize(url, is);
        return new InvocationObjectInput(input);
    }

}