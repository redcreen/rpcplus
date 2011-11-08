package com.redcreen.rpcplus.support;

import com.redcreen.rpcplus.support.Constants.ChannelConstants;


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
    
    public static String getSerialization(URL url) {
        return url.getParameter(ChannelConstants.SERIALIZATION_KEY, ChannelConstants.SERIALIZATION_DEFAULT);
    }
    
    public static int getConnectQueueLimit(URL url){
        return url.getParameter(Constants.CONNECT_QUENE_LIMIT_KEY, Constants.CONNECT_QUENE_LIMIT_SIZE);
        
    }
    
    public static String getThreadName(URL url){
        return url.getParameter(Constants.THREAD_NAME_KEY, Constants.THREAD_NAME_DEFAULT);
    }
    
    
}
