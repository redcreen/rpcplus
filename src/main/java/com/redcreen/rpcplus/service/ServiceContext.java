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

import com.redcreen.rpcplus.support.URL;

public interface ServiceContext {

    <T> void export(Invoker<T> invoker) throws InvokeException;

    <T> void unexport(Invoker<T> invoker) throws InvokeException;

    <T> Invoker<T> refer(Class<T> type, URL url) throws InvokeException;

    <T> void unrefer(Class<T> type, URL url) throws InvokeException;

    void destroy();

}
