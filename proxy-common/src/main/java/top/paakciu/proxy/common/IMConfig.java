package top.paakciu.proxy.common;


/**
 * @author paakciu
 * @ClassName: IMConfig
 * @date: 2021/7/17 15:31
 */
public class IMConfig {
    /**
     * 客户端服务端通用的配置
     */
    //服务器的地址和端口
    public static final String HOST="localhost";
    public static final int PORT=4396;
    //魔数的字节流
    public static final byte[] MAGIC=new byte[]{'P','a','a','k'};
    //如果是为了读4个字节方便，可以使用魔数对应的int来比较结果，但是跟上者一定是要一一对应的。
    public static final int MAGICINT=1348559211;

    /**
     * 客户端部分配置参数
     */
    //客户端连接的重试次数
    public static final int ClientConnectionRetry=5;

}
