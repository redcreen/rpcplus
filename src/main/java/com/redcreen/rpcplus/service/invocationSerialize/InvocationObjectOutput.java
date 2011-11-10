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

import com.redcreen.rpcplus.handler.codec.serialize.ObjectOutput;
import com.redcreen.rpcplus.service.Invocation;
import com.redcreen.rpcplus.support.Constants;
import com.redcreen.rpcplus.support.Constants.ServiceConstants;
import com.redcreen.rpcplus.util.ReflectUtils;

public class InvocationObjectOutput implements ObjectOutput {

    
    private final ObjectOutput out;
    
    public InvocationObjectOutput(ObjectOutput out) {
        this.out = out;
    }
    
    public void writeBool(boolean v) throws IOException {
        out.writeObject(v);
    }

    public void writeByte(byte v) throws IOException {
        out.writeObject(v);
    }

    public void writeShort(short v) throws IOException {
        out.writeObject(v);
    }

    public void writeInt(int v) throws IOException {
        out.writeObject(v);
    }

    public void writeLong(long v) throws IOException {
        out.writeObject(v);
    }

    public void writeFloat(float v) throws IOException {
        out.writeObject(v);
    }

    public void writeDouble(double v) throws IOException {
        out.writeObject(v);
    }

    public void writeUTF(String v) throws IOException {
        out.writeObject(v);
    }

    public void writeBytes(byte[] b) throws IOException {
        out.writeBytes(b);
    }

    public void writeBytes(byte[] b, int off, int len) throws IOException {
        out.writeBytes(b, off, len);
    }

    public void writeObject(Object obj) throws IOException {
        if (!(obj instanceof Invocation)){
            out.writeObject(obj);    
        } else {
            Invocation inv = (Invocation) obj;
            writeUTF(inv.getAttachment(Constants.VERSION_KEY, Constants.VERSION_VALUE));
            writeUTF(inv.getAttachment(ServiceConstants.INTERFACE_KEY));
            writeUTF(inv.getAttachment(ServiceConstants.VERSION_KEY));
            writeUTF(inv.getMethodName());
            writeUTF(ReflectUtils.getDesc(inv.getParameterTypes()));
            Object[] args = inv.getArguments();
            if (args != null)
            for (int i = 0; i < args.length; i++){
                writeObject(args[i]);
            }
            writeObject(inv.getAttachments());
        }
    }

    public void flushBuffer() throws IOException {
        out.flushBuffer();
    }

}