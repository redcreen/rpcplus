package com.redcreen.rpcplus.service.context;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.redcreen.rpcplus.channel.Channel;
import com.redcreen.rpcplus.channel.ChannelException;
import com.redcreen.rpcplus.channel.ChannelHandler;
import com.redcreen.rpcplus.channel.Client;
import com.redcreen.rpcplus.channel.Peer;
import com.redcreen.rpcplus.channel.Replier;
import com.redcreen.rpcplus.channel.Server;
import com.redcreen.rpcplus.handler.AbstractHandler;
import com.redcreen.rpcplus.handler.codec.CodecHandlerWrapper;
import com.redcreen.rpcplus.handler.exchange.ExchangeHandlerWrapper;
import com.redcreen.rpcplus.handler.execution.ExecutionHandlerWrapper;
import com.redcreen.rpcplus.handler.portunification.PortunificationHandlerWrapper;
import com.redcreen.rpcplus.service.Invocation;
import com.redcreen.rpcplus.service.InvokeException;
import com.redcreen.rpcplus.service.Invoker;
import com.redcreen.rpcplus.service.ServiceContext;
import com.redcreen.rpcplus.support.Extension;
import com.redcreen.rpcplus.support.ExtensionLoader;
import com.redcreen.rpcplus.support.URL;
import com.redcreen.rpcplus.support.URLUtils;
import com.redcreen.rpcplus.util.ConcurrentHashSet;

@Extension("rpc")
public class ChannelBasedServiceContext implements ServiceContext {
    private final ConcurrentMap<String, Server>     servers  = new ConcurrentHashMap<String, Server>();

    private final ConcurrentMap<String, Invoker<?>> services = new ConcurrentHashMap<String, Invoker<?>>();
    
    private final ConcurrentHashSet<Invoker<?>> invokers = new ConcurrentHashSet<Invoker<?>>();

    private class RpcHandler extends AbstractHandler implements Replier {
        @Override
        public Object reply(Channel channel, Object message) {
            Invocation inv = (Invocation) message;
            URL url = channel.getUrl();
            //TODO SERVICE KEY GET FORM ATTATCH
            String serviceKey = URLUtils.getServiceKey(url);
            return services.get(serviceKey).invoke(inv);
        }
    }
    
    private ChannelHandler getHandler(URL url){
        ChannelHandler handlerWrapper = new PortunificationHandlerWrapper(url,
                new ExecutionHandlerWrapper(url,  
                new CodecHandlerWrapper(url, 
                new ExchangeHandlerWrapper(url, 
                new RpcHandler()
                ))));
        return handlerWrapper; 
    }

    @Override
    public <T> void export(Invoker<T> invoker) throws InvokeException {
        URL url = invoker.getUrl();
        Server server = servers.get(url.getAddress());
        if (server == null) {
            Server server_t = (Server) ExtensionLoader.getExtensionLoader(Peer.class).getExtension(
                    URLUtils.getServer(url));
            Server ret = servers.putIfAbsent(url.getAddress(), server_t);
            if (ret == null) {
                try {
                    server_t.start(url, getHandler(url));
                } catch (ChannelException e) {
                    throw new InvokeException(e);
                }
                server = server_t;
            } else {
                server = ret;
            }
        }

        services.put(URLUtils.getServiceKey(url), invoker);

    }

    @Override
    public <T> void unexport(Invoker<T> invoker) throws InvokeException {

    }

    @Override
    public <T> Invoker<T> refer(Class<T> type, URL url) throws InvokeException {
        Client client = null;
        try {
            client = initClient(url);
        } catch (ChannelException e) {
            throw new InvokeException(e);
        }
        Invoker<T> invoker = new ChannelBasedServiceInvoker<T>(type, url, client);
        invokers.add(invoker);
        return invoker;
    }

    @Override
    public <T> void unrefer(Class<T> type, URL url) throws InvokeException {

    }

    @Override
    public void destroy() {
        //TODO
    }

    private Client initClient(URL url) throws ChannelException {
        Client client = (Client) ExtensionLoader.getExtensionLoader(Peer.class).getExtension(
                URLUtils.getClient(url));
        client.start(url, getHandler(url));
        return client;
    }

}
