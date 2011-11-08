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
package com.redcreen.rpcplus;

import com.redcreen.rpcplus.support.API;
import com.redcreen.rpcplus.support.Prototype;
import com.redcreen.rpcplus.support.ThreadSafe;

/**
 * RPC Exception. 
 * 
 * @serial Don't change the class name and properties.
 */
@API
@Prototype
@ThreadSafe
public class InvokeException extends RuntimeException {

    private static final long serialVersionUID    = 7815426752583648734L;

    public static final int   UNKNOWN_EXCEPTION   = 0;

    public static final int   NETWORK_EXCEPTION   = 1;

    public static final int   TIMEOUT_EXCEPTION   = 2;

    public static final int   BIZ_EXCEPTION       = 3;

    public static final int   FORBIDDEN_EXCEPTION = 4;

    private int               code;

    public InvokeException(){
        super();
    }

    public InvokeException(String message, Throwable cause){
        super(message, cause);
    }

    public InvokeException(String message){
        super(message);
    }

    public InvokeException(Throwable cause){
        super(cause);
    }

    public InvokeException(int code){
        super();
        this.code = code;
    }

    public InvokeException(int code, String message, Throwable cause){
        super(message, cause);
        this.code = code;
    }

    public InvokeException(int code, String message){
        super(message);
        this.code = code;
    }

    public InvokeException(int code, Throwable cause){
        super(cause);
        this.code = code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public boolean isTimeout() {
        return code == TIMEOUT_EXCEPTION;
    }

    public boolean isBiz() {
        return code == BIZ_EXCEPTION;
    }

    public boolean isForbidded() {
        return code == FORBIDDEN_EXCEPTION;
    }

    public boolean isNetwork() {
        return code == NETWORK_EXCEPTION;
    }

}
