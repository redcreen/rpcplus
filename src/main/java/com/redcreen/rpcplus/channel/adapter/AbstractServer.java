/*
 * Copyright 2011 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.redcreen.rpcplus.channel.adapter;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redcreen.rpcplus.channel.ChannelException;
import com.redcreen.rpcplus.channel.ChannelHandler;
import com.redcreen.rpcplus.channel.Server;
import com.redcreen.rpcplus.support.Constants;
import com.redcreen.rpcplus.support.URL;
import com.redcreen.rpcplus.util.ExecutorUtils;

public abstract class AbstractServer implements Server {
    private InetSocketAddress localAddress;

    private InetSocketAddress bindAddress;

    private int               idleTimeout;

    protected URL         url;
    
    protected ChannelHandler handler;

    protected Logger    logger = LoggerFactory.getLogger(getClass());

    ExecutorService           executor;

    public void start(URL url, ChannelHandler handler) throws ChannelException {
        this.url = url;
        this.handler = handler;
        localAddress = url.toInetSocketAddress();
        bindAddress = new InetSocketAddress(url.getHost(), url.getPort());
        this.idleTimeout = url.getParameter(Constants.IDLE_TIMEOUT_KEY,
                Constants.IDLE_TIMEOUT_DEFAULT);
        try {
            doOpen();
            if (logger.isInfoEnabled()) {
                logger.info("Start " + getClass().getSimpleName() + " bind " + getBindAddress()
                        + ", export " + getLocalAddress());
            }
        } catch (Throwable t) {
            throw new ChannelException(t);
        }
    }

    protected abstract void doOpen() throws Throwable;

    protected abstract void doClose() throws Throwable;

    public void close(int timeout) throws ChannelException {
        ExecutorUtils.shutdownNow(executor, timeout);
        try {
            doClose();
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
    }

    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    public InetSocketAddress getBindAddress() {
        return bindAddress;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

    public URL getURL() {
        return url;
    }
}
