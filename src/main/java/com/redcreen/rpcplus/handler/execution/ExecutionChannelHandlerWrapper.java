package com.redcreen.rpcplus.handler.execution;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redcreen.rpcplus.channel.Channel;
import com.redcreen.rpcplus.channel.ChannelException;
import com.redcreen.rpcplus.channel.ChannelHandler;
import com.redcreen.rpcplus.handler.AbstractChannelHandlerWrapper;
import com.redcreen.rpcplus.handler.execution.ChannelEventRunnable.ChannelState;
import com.redcreen.rpcplus.support.URL;
import com.redcreen.rpcplus.util.NamedThreadFactory;
import com.redcreen.rpcplus.util.URLUtils;

public class ExecutionChannelHandlerWrapper extends AbstractChannelHandlerWrapper {
    
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected ExecutorService executor_temp;
    
    protected final ChannelHandler handler;
    public ExecutionChannelHandlerWrapper(URL url, ChannelHandler handler) {
        super(url, handler);
        this.handler = handler;
    }
    @Override
    public void connected(Channel channel) throws ChannelException {
        invoke(channel, ChannelState.CONNECTED, null, getConnectionExecutorService(channel));
    }
    @Override
    public void disconnected(Channel channel) throws ChannelException {
        invoke(channel, ChannelState.DISCONNECTED, null, getConnectionExecutorService(channel));
    }
    @Override
    public void sent(Channel channel, Object message) throws ChannelException {
        invoke(channel, ChannelState.SENT, message);
    }
    @Override
    public void received(Channel channel, Object message) throws ChannelException {
        invoke(channel, ChannelState.RECEIVED, message);
    }
    @Override
    public void caught(Channel channel, Throwable exception) throws ChannelException {
        invoke(channel, ChannelState.CAUGHT, null);
    }
    
    private void invoke(Channel channel, ChannelState state, Object message) throws ChannelException {
        invoke(channel, state, message, getExecutorService(channel));
    }
    
    private void invoke(Channel channel, ChannelState state, Object message, ExecutorService executor) throws ChannelException {
        try{
            executor.execute(new ChannelEventRunnable(channel, handler ,state, message));
        }catch (Throwable t) {
            throw new ChannelException(channel, getClass()+" error when process "+state+" event ." , t);
        }
    }
    
    protected ExecutorService getExecutorService(Channel channel){
        return getExecutorService(channel, "executor");
    }
    
    protected ExecutorService getExecutorService(Channel channel, String key){
        
        ExecutorService executor = (ExecutorService)channel.getAttribute(key);
        if (executor == null || executor.isShutdown()) { 
            executor = (ThreadPoolExecutor)channel.getAttribute("executor.shared");
        }
        if (executor_temp == null || executor == null || executor.isShutdown() ) {
            executor = executor_temp = Executors.newCachedThreadPool(new NamedThreadFactory(URLUtils.getThreadName(channel.getUrl()), true));
        } else {
            executor = executor_temp;
        }
        return executor;
    }
    protected ExecutorService getConnectionExecutorService(Channel channel){
        return getExecutorService(channel, "executor.connect");
    }
}
