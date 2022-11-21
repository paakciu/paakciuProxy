package top.paakciu.proxy.common;


import java.util.concurrent.TimeUnit;

/**
 * @author paakciu
 * @ClassName: IMConfig
 * @date: 2021/7/17 15:31
 */
public class IMConfig {

    /**
     * 服务端端连接空闲时间
     */
    public static final int SERVER_IDLE_TIME=60;
    /**
    * 时间单位
     */
    public static final TimeUnit SERVER_TIME_UNIT=TimeUnit.SECONDS;
    //客户端连接空闲时间,一般定为服务器的3分之一
    public static final int CLIENT_IDLE_TIME=20;
    //客户端心跳包的发送间隔
    public static final int CLIENT_HEARTBEAT_INTERVAL=3;
    //时间单位
    public static final TimeUnit CLIENT_TIME_UNIT=TimeUnit.SECONDS;
}
