package com.redcreen.rpcplus.service.proxy.jdk;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.redcreen.rpcplus.service.Invoker;
import com.redcreen.rpcplus.service.proxy.InvokerHandler;
import com.redcreen.rpcplus.service.proxy.InvokerWrapper;
import com.redcreen.rpcplus.service.proxy.ProxyFactory;
import com.redcreen.rpcplus.support.Extension;
import com.redcreen.rpcplus.support.URL;

@Extension("jdk")
public class JdkProxyFactory implements ProxyFactory {

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Invoker<T> invoker, Class<?>... types) {
        Class<?>[] interfaces;
        if (types != null && types.length > 0) {
            interfaces = new Class<?>[types.length + 1];
            interfaces[0] = invoker.getInterface();
            System.arraycopy(types, 0, interfaces, 1, types.length);
        } else {
            interfaces = new Class<?>[] {invoker.getInterface()};
        }
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), interfaces, new InvokerHandler(invoker));
    }

    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) {
        return new InvokerWrapper<T>(proxy, type, url) {
            @Override
            protected Object doInvoke(T proxy, String methodName, 
                                      Class<?>[] parameterTypes, 
                                      Object[] arguments) throws Throwable {
                Method method = proxy.getClass().getMethod(methodName, parameterTypes);
                return method.invoke(proxy, arguments);
            }
        };
    }

}