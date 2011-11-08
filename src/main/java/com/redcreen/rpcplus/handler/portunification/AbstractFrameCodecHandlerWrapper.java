package com.redcreen.rpcplus.handler.portunification;

import java.io.IOException;
import java.io.InputStream;

import com.redcreen.rpcplus.channel.Channel;
import com.redcreen.rpcplus.channel.ChannelException;
import com.redcreen.rpcplus.channel.ChannelHandler;
import com.redcreen.rpcplus.handler.AbstractHandlerWrapper;
import com.redcreen.rpcplus.support.Constants.ChannelConstants;
import com.redcreen.rpcplus.support.URL;
import com.redcreen.rpcplus.util.io.Bytes;
import com.redcreen.rpcplus.util.io.UnsafeByteArrayInputStream;

public abstract class AbstractFrameCodecHandlerWrapper extends AbstractHandlerWrapper {
    
    private final int            bufferSize;
    
    private int    mOffset = 0, mLimit = 0;

    private byte[] mBuffer = null;

    public AbstractFrameCodecHandlerWrapper(URL url, ChannelHandler handler) {
        super(url, handler);
        //TODO
        this.bufferSize = 8096;
    }

    public void sent(Channel channel, Object message) throws ChannelException {
        handler.sent(channel, message);
    }
    
    public void received(Channel channel, Object message) throws ChannelException {
        if (!(message instanceof InputStream)) {
            handler.received(channel, message);
            return ;
        }
        try{
            doReceived(channel, (InputStream)message);
        }catch (IOException e) {
            throw new ChannelException(channel,e);
        }
    }

    public void doReceived(Channel channel, InputStream input) throws ChannelException, IOException {
        int readable = input.available();
        if (readable <= 0) {
            return;
        }

        int off, limit;
        byte[] buf = mBuffer;
        if (buf == null) {
            buf = new byte[bufferSize];
            off = limit = 0;
        } else {
            off = mOffset;
            limit = mLimit;
        }

        boolean remaining = true;
        Object msg;
        UnsafeByteArrayInputStream bis;
        try {
            do {
                // read data into buffer.
                int read = Math.min(readable, buf.length - limit);
//                input.readBytes(buf, limit, read);
                input.read(buf, limit, read);
                limit += read;
                readable -= read;
                bis = new UnsafeByteArrayInputStream(buf, off, limit - off); // 不需要关闭
                // decode object.
                do {
                    try {
                        msg = decode(channel, bis);
                    } catch (IOException t) {
                        remaining = false;
                        throw t;
                    }
                    if (msg == null) {
                        if (off == 0) {
                            if (readable > 0) {
                                buf = Bytes.copyOf(buf, buf.length << 1);
                            }
                        } else {
                            int len = limit - off;
                            System.arraycopy(buf, off, buf, 0, len); // adjust buffer.
                            off = 0;
                            limit = len;
                        }
                        break;
                    } else {
                        int pos = bis.position();
                        if (off == pos) {
                            remaining = false;
                            throw new IOException("Decode without read data.");
                        }
                        // null represent need more .do nothing waitfor next data package.
                        handler.received(channel, msg);
                        bis.mark(bis.position());
                        off = pos;
                    }
                } while (bis.available() > 0);
            } while (readable > 0);
        } finally {
            if (remaining) {
                int len = limit - off;
                if (len < buf.length / 2) {
                    System.arraycopy(buf, off, buf, 0, len);
                    off = 0;
                    limit = len;
                }
                mBuffer = buf;
                mOffset = off;
                mLimit = limit;
            } else {
                mBuffer = null;
                mOffset = mLimit = 0;
            }
        }
        
        
    }
    
    protected void checkPayload(Channel channel, long size) throws IOException {
        int payload = ChannelConstants.PAYLOAD_DEFAULT;
        if (channel != null && channel.getUrl() != null) {
            payload = channel.getUrl().getPositiveParameter(ChannelConstants.PAYLOAD_KEY, ChannelConstants.PAYLOAD_DEFAULT);
        }
        if (size > payload) {
            throw new IOException("Data length too large: " + size + ", max payload: " + payload + ", channel: " + channel);
        }
    }
    
//  TODO IOEXCEPTION
    protected Object decode(Channel channel, InputStream message) throws IOException {
        return message;
    }

    protected Object encode(Channel channel, Object message)  throws ChannelException{
        return message;
    }

}
