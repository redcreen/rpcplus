package com.redcreen.rpcplus.channel.adapter.netty;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.redcreen.rpcplus.channel.Channel;
import com.redcreen.rpcplus.channel.ChannelException;
import com.redcreen.rpcplus.channel.Future;
import com.redcreen.rpcplus.channel.Replier;
import com.redcreen.rpcplus.channel.adapter.Client;
import com.redcreen.rpcplus.channel.adapter.Server;
import com.redcreen.rpcplus.handler.AbstractChannelHandler;
import com.redcreen.rpcplus.handler.codec.CodecChannelHandler;
import com.redcreen.rpcplus.handler.exchange.ExchangeChannelHandlerWrapper;
import com.redcreen.rpcplus.handler.portunification.PortunificationHandler;
import com.redcreen.rpcplus.support.URL;

public class NettyServerTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testStartServer() throws ChannelException, InterruptedException {
        URL url = URL.valueOf("hold://127.0.0.1:9911?codec=exchange");
        @SuppressWarnings("unused")
        Server server = new NettyServer(url, new PortunificationHandler(url,
                new CodecChannelHandler(url, new ExchangeChannelHandlerWrapper(
                        new MockServerChannelHandler()))));
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void testStartClient() throws ChannelException, InterruptedException {
//        testStartServer();
        
        URL url = URL.valueOf("hold://127.0.0.1:9911?codec=exchange&timeout=500000");
        Client client = new NettyClient(url, new PortunificationHandler(url,
                new CodecChannelHandler(url, new ExchangeChannelHandlerWrapper(
                        new MockClientChannelHandler()))));
        Person person = new Person("charles", 4);
        Future fu = client.getChannel().request(person);
//        Thread.sleep(100);
        Future fu2 = client.getChannel().request(person);
        Object obj = fu.get();
        Object obj2 = fu2.get();
        System.out.println("ret:"+obj);
        System.out.println("ret2:"+obj2);
        
        Thread.sleep(100);
        client.getChannel().request(person);
//        Thread.sleep(Integer.MAX_VALUE);
    }

    private static class MockServerChannelHandler extends AbstractChannelHandler implements Replier {
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

    private static class MockClientChannelHandler extends AbstractChannelHandler implements Replier {
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
