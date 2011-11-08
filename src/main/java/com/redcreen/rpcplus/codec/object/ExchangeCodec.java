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
package com.redcreen.rpcplus.codec.object;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redcreen.rpcplus.channel.Channel;
import com.redcreen.rpcplus.channel.support.Request;
import com.redcreen.rpcplus.channel.support.Response;
import com.redcreen.rpcplus.codec.AbstractCodec;
import com.redcreen.rpcplus.serialize.ObjectInput;
import com.redcreen.rpcplus.serialize.Serialization;
import com.redcreen.rpcplus.support.Extension;
import com.redcreen.rpcplus.util.StringUtils;
import com.redcreen.rpcplus.util.io.Bytes;
import com.redcreen.rpcplus.util.io.UnsafeByteArrayInputStream;

@Extension(ExchangeCodec.NAME)
public class ExchangeCodec extends AbstractCodec {
    public static final String NAME = "exchange";

    private static final Logger             logger               = LoggerFactory
                                                                         .getLogger(ExchangeCodec.class);

    // header length.
    protected static final int              HEADER_LENGTH        = 16;

    // magic header.
    protected static final short            MAGIC                = (short) 0xdabb;

    protected static final byte             MAGIC_HIGH           = (byte) Bytes.short2bytes(MAGIC)[0];

    protected static final byte             MAGIC_LOW            = (byte) Bytes.short2bytes(MAGIC)[1];

    // message flag.
    protected static final byte             FLAG_REQUEST         = (byte) 0x80;

    protected static final byte             FLAG_TWOWAY          = (byte) 0x40;

    protected static final byte             FLAG_HEARTBEAT       = (byte) 0x20;

    protected static final int              SERIALIZATION_MASK   = 0x1f;

    public void encode(Channel channel, OutputStream os, Object msg) throws IOException {
        //TODO
    }

    @Override
    public boolean recognize(InputStream is) {
        byte[] header;
        try {
            header = new byte[Math.min(is.available(), 2)];
            is.read(header, 0, header.length);
            is.reset();
            if (header.length == 0) {
                return true;
            } else if (header.length == 1 && header[0] == MAGIC_HIGH) {
                return true;
            } else if (header.length >= 2 && header[0] == MAGIC_HIGH && header[1] == MAGIC_LOW) {
                return true;
            }
        } catch (IOException e) {
            logger.error("inputstream read error.", e);
        }
        return false;
    }

    public Object read(Channel channel, InputStream is) throws IOException {
        int readable = is.available();
        byte[] header = new byte[Math.min(readable, HEADER_LENGTH)];
        is.read(header);
        if (readable < HEADER_LENGTH) {
            return null;
        }
        // get data length.
        int len = Bytes.bytes2int(header, 12);
        checkPayload(channel, len);

        int tt = len + HEADER_LENGTH;
        if (readable < tt) {
            return null;
        }
        //heap copy TODO zero copy
        byte[] buf = new byte[tt];
        System.arraycopy(header, 0, buf, 0, header.length);
        is.read(buf, header.length, len);

        UnsafeByteArrayInputStream bis = new UnsafeByteArrayInputStream(buf);
        return bis;
    }

    @Override
    public Object decode(Channel channel, Object input) throws IOException {
        UnsafeByteArrayInputStream is = (UnsafeByteArrayInputStream)input;
        byte[] header = new byte[HEADER_LENGTH];
        is.read(header);
        
        byte flag = header[2], proto = (byte)( flag & SERIALIZATION_MASK );
        Serialization s = getSerializationById(proto);
        if (s == null) {
            s = getSerialization(channel);
        }
        ObjectInput in = s.deserialize(channel.getUrl(), is);
        // get request id.
        long id = Bytes.bytes2long(header, 4);
        if( ( flag & FLAG_REQUEST ) == 0 ) {
            // decode response.
            Response res = new Response(id);
            res.setHeartbeat( ( flag & FLAG_HEARTBEAT ) != 0 );
            // get status.
            byte status = header[3];
            res.setStatus(status);
            if( status == Response.OK ) {
                try {
                    Object data;
                    if (res.isHeartbeat()) {
                        data = decodeHeartbeatData(channel, in);
                    } else {
                        data = decodeResponseData(channel, in);
                    }
                    res.setResult(data);
                } catch (Throwable t) {
                    res.setStatus(Response.CLIENT_ERROR);
                    res.setErrorMessage(StringUtils.toString(t));
                }
            } else {
                res.setErrorMessage(in.readUTF());
            }
            return res;
        } else {
            // decode request.
            Request req = new Request(id);
            req.setVersion("2.0.0");
            req.setTwoWay( ( flag & FLAG_TWOWAY ) != 0 );
            req.setHeartbeat( ( flag & FLAG_HEARTBEAT ) != 0 );
            try {
                Object data;
                if (req.isHeartbeat()) {
                    data = decodeHeartbeatData(channel, in);
                } else {
                    data = decodeRequestData(channel, in);
                }
                req.setData(data);
            } catch (Throwable t) {
                // bad request
                req.setBroken(true);
                req.setData(t);
            }
            return req;
        }
    }
    
    protected Object decodeResponseData(Channel channel, ObjectInput in) throws IOException {
        return decodeData(channel, in);
    }
    protected Object decodeHeartbeatData(Channel channel, ObjectInput in) throws IOException {
        return decodeData(channel, in);
    }

    protected Object decodeRequestData(Channel channel, ObjectInput in) throws IOException {
        return decodeData(channel, in);
    }
    
    protected Object decodeData(Channel channel, ObjectInput in) throws IOException {
        try {
            return in.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException(StringUtils.toString("Read object failed.", e));
        }
    }
}
