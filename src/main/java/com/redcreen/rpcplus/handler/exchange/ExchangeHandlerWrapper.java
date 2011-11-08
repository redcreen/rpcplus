package com.redcreen.rpcplus.handler.exchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redcreen.rpcplus.channel.Channel;
import com.redcreen.rpcplus.channel.ChannelException;
import com.redcreen.rpcplus.channel.ChannelHandler;
import com.redcreen.rpcplus.channel.Replier;
import com.redcreen.rpcplus.channel.support.DefaultFuture;
import com.redcreen.rpcplus.channel.support.Request;
import com.redcreen.rpcplus.channel.support.Response;
import com.redcreen.rpcplus.handler.AbstractHandlerWrapper;
import com.redcreen.rpcplus.support.URL;
import com.redcreen.rpcplus.util.StringUtils;

public class ExchangeHandlerWrapper extends AbstractHandlerWrapper {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeHandlerWrapper.class);
    
    public ExchangeHandlerWrapper(URL url, ChannelHandler handler) {
        super(url, handler);
    }

    @Override
    public void received(Channel channel, Object message) throws ChannelException {
        handler.received(channel, message);
        if (message instanceof Request) {
            // handle request.
            Request request = (Request) message;
            if (request.isTwoWay()) {
                Response response = handleRequest(channel, request);
                if (response == null) {
                    throw new ChannelException(channel, "Response is null.");
                }
                channel.send(response);
            } 
        } else if (message instanceof Response) {
            handleResponse(channel, (Response) message);
        } 
    }
    
    Response handleRequest(Channel channel, Request req) throws ChannelException {
        Response res = new Response(req.getId(), req.getVersion());
        if (req.isHeartbeat()) {
            res.setHeartbeat(true);
            return res;
        }

        if (req.isBroken()) {
            Object data = req.getData();

            String msg;
            if (data == null) msg = null;
            else if (data instanceof Throwable) msg = StringUtils.toString((Throwable) data);
            else msg = data.toString();
            res.setErrorMessage("Fail to decode request due to: " + msg);
            res.setStatus(Response.BAD_REQUEST);

            return res;
        }

        // find handler by message class.
        Object msg = req.getData();
        if (handler == null) {// no handler.
            res.setStatus(Response.SERVICE_NOT_FOUND);
            res.setErrorMessage("InvokeHandler not found, Unsupported protocol object: " + msg);
        } else {
            try {
                // handle data.
                if (handler instanceof Replier){
                    Object result = ((Replier)handler).reply(channel, msg);
                    res.setStatus(Response.OK);
                    res.setResult(result);
                } else {
                    String errormsg = "InvokeHandler not implement Replier interface .can not use exchange wrapper.";
                    res.setStatus(Response.SERVICE_ERROR);
                    res.setErrorMessage(errormsg);
                    if (logger.isWarnEnabled()) {
                        logger.warn(errormsg, new IllegalStateException(errormsg));
                    }
                }
            } catch (Throwable e) {
                res.setStatus(Response.SERVICE_ERROR);
                res.setErrorMessage(StringUtils.toString(e));
            }
        }
        return res;
    }

    static void handleResponse(Channel channel, Response response) throws ChannelException {
        if (response != null && !response.isHeartbeat()) {
            DefaultFuture.received(channel, response);
        }
    }
}
