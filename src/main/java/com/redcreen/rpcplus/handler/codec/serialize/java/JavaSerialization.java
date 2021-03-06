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
package com.redcreen.rpcplus.handler.codec.serialize.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.redcreen.rpcplus.handler.codec.serialize.ObjectInput;
import com.redcreen.rpcplus.handler.codec.serialize.ObjectOutput;
import com.redcreen.rpcplus.handler.codec.serialize.Serialization;
import com.redcreen.rpcplus.support.Extension;
import com.redcreen.rpcplus.support.URL;

@Extension("java")
public class JavaSerialization implements Serialization {

    public byte getContentTypeId() {
        return 3;
    }

    public String getContentType() {
        return "x-application/java";
    }

    public ObjectOutput serialize(URL url, OutputStream out) throws IOException {
        return new JavaObjectOutput(out);
    }

    public ObjectInput deserialize(URL url, InputStream is) throws IOException {
        return new JavaObjectInput(is);
    }

}