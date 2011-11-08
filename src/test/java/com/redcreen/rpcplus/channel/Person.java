/**
 * Project: rpcplus
 * 
 * File Created at 2011-11-8
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
package com.redcreen.rpcplus.channel;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final AtomicInteger idincrement = new AtomicInteger(); 
    private int     id;
    private String            name;
    private int               age;

    public Person(String name, int age) {
        this.id = idincrement.incrementAndGet();
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name + ", age=" + age + "]";
    }
}