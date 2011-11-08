package com.redcreen.rpcplus.handler.execution;

import com.redcreen.rpcplus.channel.Channel;
import com.redcreen.rpcplus.channel.ChannelHandler;

public class ChannelEventRunnable implements Runnable {
    private final ChannelHandler handler;
    private final Channel channel;
    private final ChannelState state;
    private final Throwable exception;
    private final Object message;
    
    public ChannelEventRunnable(Channel channel, ChannelHandler handler, ChannelState state) {
        this(channel, handler, state, null);
    }
    
    public ChannelEventRunnable(Channel channel, ChannelHandler handler, ChannelState state, Object message) {
        this(channel, handler, state, message, null);
    }
    
    public ChannelEventRunnable(Channel channel, ChannelHandler handler, ChannelState state, Throwable t) {
        this(channel, handler, state, null , t);
    }

    public ChannelEventRunnable(Channel channel, ChannelHandler handler, ChannelState state, Object message, Throwable exception) {
        this.channel = channel;
        this.handler = handler;
        this.state = state;
        this.message = message;
        this.exception = exception;
    }
    
    public void run() {
        switch (state) {
            case CONNECTED:
                try{
                    handler.connected(channel);
                }catch (Exception e) {
                    throw new RuntimeException("ChannelEventRunnable handle error,channel is "+channel,e);
                }
                break;
            case DISCONNECTED:
                try{
                    handler.disconnected(channel);
                }catch (Exception e) {
                    throw new RuntimeException("ChannelEventRunnable handle error,channel is "+channel,e);
                }
                break;
            case SENT:
                try{
                    handler.sent(channel,message);
                }catch (Exception e) {
                    throw new RuntimeException("ChannelEventRunnable handle error,channel is "+channel+",message is "+ message,e);
                }
                
                break;
            case RECEIVED:
                try{
                    handler.received(channel, message);
                }catch (Exception e) {
                    throw new RuntimeException("ChannelEventRunnable handle error,channel is "+channel+",message is "+ message,e);
                }
                break;
            case CAUGHT:
                try{
                    handler.caught(channel, exception);
                }catch (Exception e) {
                    throw new RuntimeException("ChannelEventRunnable handle error,channel is "+channel +", message is: "+message+" exception is "+exception,e);
                }
                
                break;
                }
        }
    
    public enum ChannelState{
        CONNECTED,DISCONNECTED,SENT,RECEIVED,CAUGHT
    }

}