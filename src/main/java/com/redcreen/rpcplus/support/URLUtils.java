package com.redcreen.rpcplus.support;

import com.redcreen.rpcplus.support.Constants.ChannelConstants;
import com.redcreen.rpcplus.support.Constants.ServiceConstants;


public class URLUtils {
    public static int getTimeout(URL url) {
        return url.getParameter(Constants.TIMEOUT_KEY, Constants.TIMEOUT_DEFAULT);
    }

    public static int getCloseTimeout(URL url) {
        return url.getParameter(Constants.CLOSE_TIMEOUT_KEY, Constants.CLOSE_TIMEOUT_KEY_DEFAULT);
    }

    public static int getIoThreads(URL url) {
        return url.getParameter(ChannelConstants.IO_THREADS_KEY, ChannelConstants.IO_THREADS_DEFAULT);

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
    
    public static String getProxy(URL url){
        return url.getParameter(ServiceConstants.PROXY_KEY, ServiceConstants.PROXY_DEFAULT);
    }
    
    public static String getServiceContext(URL url){
        return url.getProtocol();
    }
    
    public static String getClient(URL url){
        return url.getParameter(ChannelConstants.CLIENT_KEY, ChannelConstants.CLIENT_DEFAULT);
    }
    
    public static String getServer(URL url){
        return url.getParameter(ChannelConstants.SERVER_KEY, ChannelConstants.SERVER_DEFAULT);
    }
    
    public static String getServiceKey(URL url){
        String inf = url.getParameter(ServiceConstants.INTERFACE_KEY, url.getPath());
        if (inf == null) return null;
        StringBuilder buf = new StringBuilder();
        String group = url.getParameter(ServiceConstants.GROUP_KEY);
        if (group != null && group.length() > 0) {
            buf.append(group).append("/");
        }
        buf.append(inf);
        String version = url.getParameter(ServiceConstants.VERSION_KEY);
        if (version != null && version.length() > 0) {
            buf.append(":").append(version);
        }
        return buf.toString();
    }
}
