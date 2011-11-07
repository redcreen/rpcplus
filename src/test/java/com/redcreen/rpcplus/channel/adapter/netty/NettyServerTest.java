package com.redcreen.rpcplus.channel.adapter.netty;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.redcreen.rpcplus.channel.Channel;
import com.redcreen.rpcplus.channel.ChannelException;
import com.redcreen.rpcplus.channel.ChannelHandler;
import com.redcreen.rpcplus.channel.adapter.Server;
import com.redcreen.rpcplus.handler.AbstractChannelHandler;
import com.redcreen.rpcplus.handler.codec.PortunificationHandler;
import com.redcreen.rpcplus.support.URL;

public class NettyServerTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void testStartServer() throws ChannelException, InterruptedException{
        URL url = URL.valueOf("hold://127.0.0.1:9911");
        @SuppressWarnings("unused")
        Server server = new NettyServer(url, new PortunificationHandler(url, handler));
        Thread.sleep(Integer.MAX_VALUE);
    }
    
    ChannelHandler handler = new AbstractChannelHandler() {
        
        @Override
        public void sent(Channel channel, Object message) throws ChannelException {
            //TODO message encode
            System.out.println("out" + message);
        }
        
        @Override
        public void received(Channel channel, Object message) throws ChannelException {
            System.out.println(message);
            channel.send("echo:"+message + "\r\n");
        }
    };
}
