/**
 * Project: rpcplus
 * 
 * File Created at 2011-11-9
 * $Id$
 * 
 * Copyright 1999-2100 Alibaba.com Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Alibaba Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Alibaba.com.
 */
package com.redcreen.rpcplus.service;


import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.redcreen.rpcplus.ServiceUtils;
import com.redcreen.rpcplus.support.URL;

public class ServiceTest {
    URL url = URL.valueOf("rpc://127.0.0.1:9911?interface="+HelloService.class.getName()+"&timeout=100000");

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    public void exportHelloService(){
        ServiceUtils.export(new HelloServiceImpl(), HelloService.class, url);
    }
    
    public void callHelloService(){
        HelloService service = ServiceUtils.refer(HelloService.class, url);
        String ret = service.say("hi");
        Assert.assertEquals("hi", ret);
    }
    
//    @Test
    public void testCall(){
        exportHelloService();
        callHelloService();
    }
    
    
    public static interface HelloService{
        public String say(String words);
    }
    
    public static class HelloServiceImpl implements HelloService{

        @Override
        public String say(String words) {
            return words;
        }
    }
}
