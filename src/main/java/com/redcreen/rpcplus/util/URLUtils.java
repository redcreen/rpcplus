/*
 * Copyright 2011 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.redcreen.rpcplus.util;

import com.redcreen.rpcplus.support.Constants;
import com.redcreen.rpcplus.support.Constants.ChannelConstants;
import com.redcreen.rpcplus.support.URL;

public class URLUtils {
    public static int getTimeout(URL url) {
        return url.getParameter(Constants.TIMEOUT_KEY, Constants.TIMEOUT_DEFAULT);
    }

    public static int getCloseTimeout(URL url) {
        return url.getParameter(Constants.CLOSE_TIMEOUT_KEY, Constants.CLOSE_TIMEOUT_KEY_DEFAULT);
    }

    public static int getIoThreads(URL url) {
        return url.getParameter(Constants.TIMEOUT_KEY, ChannelConstants.IO_THREADS_DEFAULT);

    }

    public static boolean getSent(URL url) {
        return url.getParameter(ChannelConstants.CHANNEL_SENT_KEY,
                ChannelConstants.CHANNEL_SENT_DEFAULT);
    }

    public static int getPayload(URL url) {
        return url.getPositiveParameter(ChannelConstants.PAYLOAD_KEY,
                ChannelConstants.PAYLOAD_DEFAULT);
    }

    public static String getCodec(URL url) {
        return url.getParameter(ChannelConstants.CODEC_KEY, ChannelConstants.CODEC_DEFAULT);
    }
}
