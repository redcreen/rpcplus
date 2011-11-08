package com.redcreen.rpcplus.support;

public class Constants {

    public final static String version                   = "0.0.1";
    public final static String SYSTEM_PREFIX_KEY         = ""; //TODO
    public final static String TIMEOUT_KEY               = SYSTEM_PREFIX_KEY + "timeout";
    public final static int    TIMEOUT_DEFAULT           = 5000;

    public final static String IDLE_TIMEOUT_KEY          = SYSTEM_PREFIX_KEY + "idle.timeout";
    public final static int    IDLE_TIMEOUT_DEFAULT      = 5000;

    public final static String CLOSE_TIMEOUT_KEY         = SYSTEM_PREFIX_KEY + "close.timeout";
    public final static int    CLOSE_TIMEOUT_KEY_DEFAULT = 5000;

    public static class ChannelConstants {

        public final static String  SERVER_THREAD_POOL_NAME = "server.handler.pool";

        public final static String  IO_THREADS_KEY          = SYSTEM_PREFIX_KEY + "ios";
        public final static int     IO_THREADS_DEFAULT      = Runtime.getRuntime()
                                                                    .availableProcessors() + 1;

        public final static String  CHANNEL_SENT_KEY        = SYSTEM_PREFIX_KEY + "sent";
        //TODO
        public final static boolean CHANNEL_SENT_DEFAULT    = false;

        public final static String  PAYLOAD_KEY             = SYSTEM_PREFIX_KEY + "payload";
        public final static int     PAYLOAD_DEFAULT         = 1024 * 1024;

        public final static String  CODEC_KEY               = SYSTEM_PREFIX_KEY + "codec";
        public final static String  CODEC_DEFAULT           = "telnet";
        
        public final static String  SERIALIZATION_KEY               = SYSTEM_PREFIX_KEY + "serialization";
        public final static String  SERIALIZATION_DEFAULT           = "java";
        

        public final static String  CHECK_KEY               = SYSTEM_PREFIX_KEY + "check";
        public final static boolean CHECK_DEFAULT           = true;

        public final static String  CHARSET_KEY             = SYSTEM_PREFIX_KEY + "charset";
        
    }
}
