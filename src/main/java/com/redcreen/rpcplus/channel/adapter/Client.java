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

import com.redcreen.rpcplus.channel.Channel;
import com.redcreen.rpcplus.channel.ChannelException;
import com.redcreen.rpcplus.channel.ChannelHandler;
import com.redcreen.rpcplus.channel.Peer;
import com.redcreen.rpcplus.support.URL;

/**
 */
public abstract class Client implements Peer {

    protected final ChannelHandler                   handler;
    protected volatile Channel                       channel;
    protected final URL                              url;
    private final Lock                               connectLock              = new ReentrantLock();

    public Client(ChannelHandler handler, URL url){
        super();
        this.handler = handler;
        this.url = url;
        try {
            doOpen();
        } catch (Throwable t) {
            close();
            // throw new ChannelException(channel, t);
        }
        try {
            // connect.
            connect();
            // if (logger.isInfoEnabled()) {
            // logger.info("Start " + getClass().getSimpleName() + " " + NetUtils.getLocalAddress() +
            // " connect to the server " + getRemoteAddress());
            // }
        } catch (ChannelException t) {
            // if (url.getParameter(Constants.CHECK_KEY, true)) {
            close();
            // throw t;
            // } else {
            // // logger.error("Failed to start " + getClass().getSimpleName() + " " + NetUtils.getLocalAddress()
            // // + " connect to the server " + getRemoteAddress() + ", cause: " + t.getMessage(), t);
            // }
        } catch (Throwable t) {
            close();
            // throw new RemotingException(url.toInetSocketAddress(), null,
            // "Failed to start " + getClass().getSimpleName() + " " + NetUtils.getLocalAddress()
            // + " connect to the server " + getRemoteAddress() + ", cause: " + t.getMessage(), t);
        }
    }

    public ChannelHandler getHandler() {
        return handler;
    }

    public Channel getChannel() {
        return channel;
    }

    public URL getURL() {
        return channel == null ? null : channel.getUrl();
    }

    public void close(int timeout) {
        try {
            channel.close(timeout);
        } catch (Exception e) {
            // TODO: handle exception
        }
        try {
            doClose();
        } catch (ChannelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void close() {
        close(5000);// TODO
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
