package com.redcreen.rpcplus.proxy;

import java.lang.reflect.InvocationTargetException;

import com.redcreen.rpcplus.Invocation;
import com.redcreen.rpcplus.InvokeException;
import com.redcreen.rpcplus.Invoker;
import com.redcreen.rpcplus.Result;
import com.redcreen.rpcplus.support.RpcResult;
import com.redcreen.rpcplus.support.URL;

public abstract class InvokerWrapper<T> implements Invoker<T> {
    
    private final T proxy;
    
    private final Class<T> type;
    
    private final URL url;

    public InvokerWrapper(T proxy, Class<T> type, URL url){
        if (proxy == null) {
            throw new IllegalArgumentException("proxy == null");
        }
        if (type == null) {
            throw new IllegalArgumentException("interface == null");
        }
        if (! type.isInstance(proxy)) {
            throw new IllegalArgumentException(proxy.getClass().getName() + " not implement interface " + type);
        }
        this.proxy = proxy;
        this.type = type;
        this.url = url;
    }

    public Class<T> getInterface() {
        return type;
    }

    public URL getUrl() {
        return url;
    }

    public boolean isAvailable() {
        return true;
    }

    public void destroy() {
    }

    public Result invoke(Invocation invocation) throws InvokeException {
        try {
            return new RpcResult(doInvoke(proxy, invocation.getMethodName(), invocation.getParameterTypes(), invocation.getArguments()));
        } catch (InvocationTargetException e) {
            return new RpcResult(e.getTargetException());
        } catch (Throwable e) {
            throw new InvokeException(e.getMessage(), e);
        }
    }
    
    protected abstract Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments) throws Throwable;

    @Override
    public String toString() {
        return getInterface() + " -> " + getUrl()==null?" ":getUrl().toString();
    }

    
}