package com.redcreen.rpcplus.handler.execution;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redcreen.rpcplus.support.URL;

public class AbortPolicyWithReport extends ThreadPoolExecutor.AbortPolicy {
    
    protected static final Logger logger = LoggerFactory.getLogger(AbortPolicyWithReport.class);
    
    private final String threadName;
    
    private final URL url;
    
    public AbortPolicyWithReport(String threadName, URL url) {
        this.threadName = threadName;
        this.url = url;
    }
    
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        String msg = String.format("Thread pool is EXHAUSTED!" +
        		" Thread Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d), Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s), in %s!" , 
                threadName, e.getPoolSize(), e.getActiveCount(), e.getCorePoolSize(), e.getMaximumPoolSize(), e.getLargestPoolSize(),
                e.getTaskCount(), e.getCompletedTaskCount(), e.isShutdown(), e.isTerminated(), e.isTerminating(), url.toIdentityString());
        logger.warn(msg);
        throw new RejectedExecutionException(msg);
    }

}