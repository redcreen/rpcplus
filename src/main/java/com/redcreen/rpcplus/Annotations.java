/*
 * Copyright 2011 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.redcreen.rpcplus;

import com.redcreen.rpcplus.Annotations.API;

/**
 */
@API
public final class Annotations {

    public static @interface Singleton {
    };

    public static @interface Prototype {
    };

    /**
     * for document
     */
    public static @interface SPI {
    }

    /**
     * for document
     */
    public static @interface API {
    }

    /**
     * for document
     */
    public static @interface ThreadSafe {
    };
}
