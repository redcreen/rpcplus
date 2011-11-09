package com.redcreen.rpcplus;

import com.redcreen.rpcplus.service.Invoker;
import com.redcreen.rpcplus.service.ServiceContext;
import com.redcreen.rpcplus.service.proxy.ProxyFactory;
import com.redcreen.rpcplus.support.ExtensionLoader;
import com.redcreen.rpcplus.support.URL;
import com.redcreen.rpcplus.support.URLUtils;

public class ServiceUtils {
    
    public static <T> void export(T ref, Class<T> type,  URL url){
        String proxyName = URLUtils.getProxy(url);
        ProxyFactory proxy = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getExtension(proxyName);
        Invoker<T> invoker = proxy.getInvoker(ref, type, url);
        
        String contextName = URLUtils.getServiceContext(url);
        ServiceContext context = ExtensionLoader.getExtensionLoader(ServiceContext.class).getExtension(contextName);
        
        context.export(invoker);
    }
    
    public static <T> T refer(Class<T> type, URL url){
        String contextName = URLUtils.getServiceContext(url);
        ServiceContext context = ExtensionLoader.getExtensionLoader(ServiceContext.class).getExtension(contextName);
        Invoker<T> invoker = context.refer(type, url);
        
        String proxyName = URLUtils.getProxy(url);
        ProxyFactory proxy = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getExtension(proxyName);
        
        return proxy.getProxy(invoker);
    }
}
