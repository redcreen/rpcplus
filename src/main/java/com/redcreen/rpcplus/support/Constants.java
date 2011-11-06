package com.redcreen.rpcplus.support;

public class Constants {

    public final static String version = "0.0.1";
    public final static String SYSTEM_PREFIX_KEY = "_";
    public final static String TIMEOUT_KEY = SYSTEM_PREFIX_KEY+"timeout";
    public final static int TIMEOUT_DEFAULT = 5000;
    
    public final static String CLOSE_TIMEOUT_KEY = SYSTEM_PREFIX_KEY+"close.timeout";
    public final static int CLOSE_TIMEOUT_KEY_DEFAULT = 5000;
    
    public static class ChannelConstants {
        public final static String IO_THREADS_KEY = SYSTEM_PREFIX_KEY+"ios";
        public final static int IO_THREADS_DEFAULT = Runtime.getRuntime().availableProcessors() + 1;
        
        public final static String CHANNEL_SENT_KEY = SYSTEM_PREFIX_KEY+"sent";
        public final static boolean CHANNEL_SENT_DEFAULT = true;
    }
}
