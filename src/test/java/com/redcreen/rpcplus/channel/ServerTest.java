package com.redcreen.rpcplus.channel;


import java.util.HashMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.redcreen.rpcplus.handler.AbstractHandler;
import com.redcreen.rpcplus.handler.codec.CodecHandlerWrapper;
import com.redcreen.rpcplus.handler.exchange.ExchangeHandlerWrapper;
import com.redcreen.rpcplus.handler.execution.ExecutionHandlerWrapper;
import com.redcreen.rpcplus.handler.portunification.PortunificationHandlerWrapper;
import com.redcreen.rpcplus.support.ExtensionLoader;
import com.redcreen.rpcplus.support.URL;

public class ServerTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

//    @Test
    public void testStartServer() throws ChannelException, InterruptedException {
        URL url = URL.valueOf("hold://127.0.0.1:9911?codec=exchange");
        Server server = (Server)(ExtensionLoader.getExtensionLoader(Peer.class).getExtension("nettyserver"));
        ChannelHandler handler = new PortunificationHandlerWrapper(url,
                                 new ExecutionHandlerWrapper(url,  
                                 new CodecHandlerWrapper(url, 
                                 new ExchangeHandlerWrapper(url, 
                                 new MockSererHandler()
                                 ))));
        server.start(url, handler);
//        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void testStartClient() throws ChannelException, InterruptedException {
        testStartServer();
        
        URL url = URL.valueOf("hold://127.0.0.1:9911?codec=exchange&timeout=500000");
        ChannelHandler handler = new PortunificationHandlerWrapper(url,
                                new ExecutionHandlerWrapper(url,  
                                new CodecHandlerWrapper(url, 
                                new ExchangeHandlerWrapper(url, 
                                new MockClientHandler()
                        ))));
        Client client = (Client)(ExtensionLoader.getExtensionLoader(Peer.class).getExtension("nettyclient"));
        client.start(url, handler);
        Person person = new Person("charles", 4);
        ChannelFuture fu = client.getChannel().request(person);
//        Thread.sleep(100);
        person = new Person("charles", 4);
        ChannelFuture fu2 = client.getChannel().request(person);
        Object obj = fu.get();
        Object obj2 = fu2.get();
        System.out.println("ret:"+obj);
        System.out.println("ret2:"+obj2);
        
        Thread.sleep(100);
        person = new Person("charles", 4);
        ChannelFuture fu3 = client.getChannel().request(person);
        System.out.println("ret3:"+ fu3.get());
//        Thread.sleep(Integer.MAX_VALUE);
    }
    
    
    private static class MockSererHandler extends AbstractHandler implements Replier {
        @Override
        public void sent(Channel channel, Object message) throws ChannelException {
            System.out.println("server out:" + message);
        }

        @Override
        public void received(Channel channel, Object message) throws ChannelException {
            System.out.println("server receive:" + message);
            //            channel.send("echo:"+message + "\r\n");
        }

        @Override
        public Object reply(Channel channel, Object message) {
            return "echo:" + message + "\r\n";
        }
    }

    private static class MockClientHandler extends AbstractHandler implements Replier {
        @Override
        public void sent(Channel channel, Object message) throws ChannelException {
            System.out.println("client out:" + message);
        }

        @Override
        public void received(Channel channel, Object message) throws ChannelException {
            System.out.println("client receive:" + message);
//            channel.send("echo:" + message + "\r\n");
        }

        @Override
        public Object reply(Channel channel, Object message) {
            return "echo:" + message + "\r\n";
        }
    }
}
