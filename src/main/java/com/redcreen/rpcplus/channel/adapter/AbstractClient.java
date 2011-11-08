/*
 * Copyright 2011 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.redcreen.rpcplus.channel.adapter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redcreen.rpcplus.channel.Channel;
import com.redcreen.rpcplus.channel.ChannelException;
import com.redcreen.rpcplus.channel.ChannelHandler;
import com.redcreen.rpcplus.channel.Client;
import com.redcreen.rpcplus.support.Constants.ChannelConstants;
import com.redcreen.rpcplus.support.URL;
import com.redcreen.rpcplus.support.URLUtils;

/**
 */
public abstract class AbstractClient implements Client {
    protected Logger                 logger      = LoggerFactory.getLogger(getClass());
    protected ChannelHandler handler;
    protected volatile Channel     channel;
    protected URL            url;
    private Lock             connectLock = new ReentrantLock();

    public void start(URL url, ChannelHandler handler) throws ChannelException {
        this.handler = handler;
        this.url = url;
        try {
            doOpen();
        } catch (Throwable t) {
            close();
            throw new ChannelException(channel, t);
        }
        try {
            connect();
        } catch (ChannelException t) {
            if (url.getParameter(ChannelConstants.CHECK_KEY, true)) {
                close();
                throw t;
            } else {
                logger.error("Failed connect to the server .url :" + url, t);
            }
        } catch (Throwable t) {
            close();
            throw new ChannelException(t);
        }
    }

    public ChannelHandler getHandler() {
        return handler;
    }

    /* (non-Javadoc)
     * @see com.redcreen.rpcplus.channel.adapter.Client1#getChannel()
     */
    @Override
    public Channel getChannel() {
        return channel;
    }

    public URL getURL() {
        return url;
    }

    public void close(int timeout) {
        try {
            if (channel != null ){
                channel.close(timeout);
            }
        } catch (Exception e) {
            logger.error("client close error ." , e);
        }
        try {
            doClose();
        } catch (ChannelException e) {
            logger.error("client close error ." , e);
        }
    }

    public void close() {
        close(URLUtils.getCloseTimeout(url));
    }

    protected void connect() throws ChannelException {
        connectLock.lock();
        try {
            doConnect();
        } catch (Exception e) {
            throw new ChannelException(e);
        } finally {
            connectLock.unlock();
        }
    }

    protected abstract void doOpen() throws ChannelException;

    protected abstract void doClose() throws ChannelException;

    protected abstract void doConnect() throws ChannelException;

    protected abstract void doDisconnect() throws ChannelException;

}
