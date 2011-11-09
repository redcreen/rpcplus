/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redcreen.rpcplus.service;

import java.io.Serializable;

import com.redcreen.rpcplus.support.API;
import com.redcreen.rpcplus.support.Prototype;
import com.redcreen.rpcplus.support.SPI;
import com.redcreen.rpcplus.support.ThreadSafe;

/**
 * RPC invoke result.
 * 
 * @serial Don't change the class name.
 */
@SPI
@API
@Prototype
@ThreadSafe
public class InvokeResult implements Serializable {

    private static final long        serialVersionUID = -6925924956850004727L;

    private Object                   result;

    private Throwable                exception;

    public InvokeResult(){
    }

    public InvokeResult(Object result){
        this.result = result;
    }

    public InvokeResult(Throwable exception){
        this.exception = exception;
    }

    public Object recreate() throws Throwable {
        if (exception != null) {
            throw exception;
        }
        return result;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable e) {
        this.exception = e;
    }

    public boolean hasException() {
        return exception != null;
    }

}