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
import java.util.HashMap;
import java.util.Map;

import com.redcreen.rpcplus.handler.codec.serialize.ObjectInput;
import com.redcreen.rpcplus.service.Invocation;
import com.redcreen.rpcplus.support.Constants;
import com.redcreen.rpcplus.support.Constants.ServiceConstants;
import com.redcreen.rpcplus.util.ReflectUtils;
import com.redcreen.rpcplus.util.StringUtils;

public class InvocationObjectInput implements ObjectInput {

    private final ObjectInput input;

    public InvocationObjectInput(ObjectInput input){
        this.input = input;
    }

    @Override
    public boolean readBool() throws IOException {
        return input.readBool();
    }

    @Override
    public byte readByte() throws IOException {
        return input.readByte();
    }

    @Override
    public short readShort() throws IOException {
        return input.readShort();
    }

    @Override
    public int readInt() throws IOException {
        return input.readInt();
    }

    @Override
    public long readLong() throws IOException {
        return input.readLong();
    }

    @Override
    public float readFloat() throws IOException {
        return input.readFloat();
    }

    @Override
    public double readDouble() throws IOException {
        return input.readDouble();
    }

    @Override
    public String readUTF() throws IOException {
        return input.readUTF();
    }

    @Override
    public byte[] readBytes() throws IOException {
        return input.readBytes();
    }
    
    private static final Object[]   EMPTY_OBJECT_ARRAY      = new Object[0];

    private static final Class<?>[] EMPTY_CLASS_ARRAY       = new Class<?>[0];

    @Override
    public Object readObject() throws IOException, ClassNotFoundException {
        return input.readObject();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
        if (cls != Invocation.class){
            return readObject(cls);
        } else {
            Invocation inv = new Invocation();
            inv.setAttachment(Constants.VERSION_KEY, readUTF());
            inv.setAttachment(ServiceConstants.INTERFACE_KEY, readUTF());
            inv.setAttachment(ServiceConstants.VERSION_KEY, readUTF());

            inv.setMethodName(readUTF());
            try {
                Object[] args;
                Class<?>[] pts;
                String desc = readUTF();
                if (desc.length() == 0) {
                    pts = EMPTY_CLASS_ARRAY;
                    args = EMPTY_OBJECT_ARRAY;
                } else {
                    pts = ReflectUtils.desc2classArray(desc);
                    args = new Object[pts.length];
                    for (int i = 0; i < args.length; i++){
                        try{
                            args[i] = readObject(pts[i]);
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                inv.setParameterTypes(pts);
                
                Map<String, String> map = (Map<String, String>) readObject(Map.class);
                if (map != null && map.size() > 0) {
                    Map<String, String> attachment = inv.getAttachments();
                    if (attachment == null) {
                        attachment = new HashMap<String, String>();
                    }
                    attachment.putAll(map);
                    inv.setAttachments(attachment);
                }
                inv.setArguments(args);
                
            } catch (ClassNotFoundException e) {
                throw new IOException(StringUtils.toString("Read invocation data failed.", e));
            }
            return (T)inv;
        }
    }
}