package com.redcreen.rpcplus.proxy.javassist;

import com.redcreen.rpcplus.Invoker;
import com.redcreen.rpcplus.proxy.InvokerHandler;
import com.redcreen.rpcplus.proxy.InvokerWrapper;
import com.redcreen.rpcplus.proxy.ProxyFactory;
import com.redcreen.rpcplus.support.Extension;
import com.redcreen.rpcplus.support.URL;
import com.redcreen.rpcplus.util.bytecode.Proxy;
import com.redcreen.rpcplus.util.bytecode.Wrapper;

@Extension("javassist")
public class JavassistProxyFactory implements ProxyFactory {

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
        return (T) Proxy.getProxy(interfaces).newInstance(new InvokerHandler(invoker));
    }

    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) {
        // TODO Wrapper类不能正确处理带$的类名
        final Wrapper wrapper = Wrapper.getWrapper(proxy.getClass().getName().indexOf('$') < 0 ? proxy.getClass() : type);
        return new InvokerWrapper<T>(proxy, type, url) {
            @Override
            protected Object doInvoke(T proxy, String methodName, 
                                      Class<?>[] parameterTypes, 
                                      Object[] arguments) throws Throwable {
                return wrapper.invokeMethod(proxy, methodName, parameterTypes, arguments);
            }
        };
    }

}