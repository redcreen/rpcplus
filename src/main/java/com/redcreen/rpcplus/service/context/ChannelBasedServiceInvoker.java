package com.redcreen.rpcplus.service.context;

import com.redcreen.rpcplus.channel.Channel;
import com.redcreen.rpcplus.channel.ChannelException;
import com.redcreen.rpcplus.channel.Client;
import com.redcreen.rpcplus.channel.support.TimeoutException;
import com.redcreen.rpcplus.service.Invocation;
import com.redcreen.rpcplus.service.InvokeException;
import com.redcreen.rpcplus.service.InvokeResult;
import com.redcreen.rpcplus.service.support.AbstractInvoker;
import com.redcreen.rpcplus.support.Constants;
import com.redcreen.rpcplus.support.Constants.ServiceConstants;
import com.redcreen.rpcplus.support.URL;
import com.redcreen.rpcplus.util.AtomicPositiveInteger;


public class ChannelBasedServiceInvoker<T> extends AbstractInvoker<T> {

    private final Client[]      clients;

    private final AtomicPositiveInteger index = new AtomicPositiveInteger();

    private final String                version;
    
    public ChannelBasedServiceInvoker(Class<T> serviceType, URL url, Client... clients){
        super(serviceType, url, new String[] {ServiceConstants.GROUP_KEY});
        this.clients = clients;
        // get version.
        this.version = url.getParameter(ServiceConstants.VERSION_KEY, "0.0.0");
    }

    @Override
    protected InvokeResult doInvoke(final Invocation inv) throws Throwable {
        inv.setAttachment(ServiceConstants.PATH_KEY, getUrl().getPath());
        inv.setAttachment(ServiceConstants.VERSION_KEY, version);
        
        Client client;
        if (clients.length == 1) {
            client = clients[0];
        } else {
            client = clients[index.getAndIncrement() % clients.length];
        }
        try {
//            // 不可靠异步
//            boolean isAsync = getUrl().getMethodParameter(methodName, ServiceConstants.ASYNC_KEY, false);
//            int timeout = getUrl().getMethodParameter(methodName, Constants.TIMEOUT_KEY,Constants.TIMEOUT_DEFAULT);
//            if (isAsync) { 
//                boolean isReturn = getUrl().getMethodParameter(methodName, ServiceConstants.RETURN_KEY, true);
//                if (isReturn) {
//                    InvokeFuture future = currentClient.request(inv, timeout) ;
//                    InvokeContext.getContext().setFuture(new FutureAdapter<Object>(future));
//                } else {
//                    boolean isSent = getUrl().getMethodParameter(methodName, ServiceConstants.SENT_KEY, false);
//                    currentClient.send(inv, isSent);
//                    InvokeContext.getContext().setFuture(null);
//                }
//                return new RpcResult();
//            }
//            InvokeContext.getContext().setFuture(null);
            return (InvokeResult) client.getChannel().request(inv).get();
        } catch (TimeoutException e) {
            throw new InvokeException(InvokeException.TIMEOUT_EXCEPTION, e.getMessage(), e);
        } catch (ChannelException e) {
            throw new InvokeException(InvokeException.NETWORK_EXCEPTION, e.getMessage(), e);
        }
    }
    
    @Override
    public boolean isAvailable() {
        if (!super.isAvailable())
            return false;
        if (clients.length ==1){
            Channel channel = clients[0].getChannel();
            return  channel== null ? false : channel.isConnected();
        } else {
            for (Client client : clients){
                Channel channel = client.getChannel();
                if (channel != null && channel.isConnected()){
                    return true;
                }
            }
        }
        return false;
    }

    public void destroy() {
        super.destroy();
        for (Client client : clients) {
            try {
                client.close(Constants.CLOSE_TIMEOUT_KEY_DEFAULT);
            } catch (Throwable t) {
                logger.warn(t.getMessage(), t);
            }
        }
    }

}