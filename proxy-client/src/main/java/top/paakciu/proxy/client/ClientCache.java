package top.paakciu.proxy.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import lombok.Data;
import top.paakciu.proxy.core.protocal.packet.special.ProxyPacket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname ClientCache
 * @Date 2022/9/25 22:27
 * @Created by paakciu
 */
public class ClientCache {

    public static String ServerIp = "";
    public static int ServerPort = 0;
    public static int LocalPort = 0;
    /**
     * 求建立连接到成功建立直接到达的数据包，将缓存起来
     * true-可以存入缓存
     * false-不存入缓存
     */
    public static Map<String,Boolean> packetStateCache = new ConcurrentHashMap<>(2);
    /**
     * 请求建立连接到成功建立直接到达的数据包，将缓存起来
     */
    public static Map<String, List<byte[]>> packetCache = new ConcurrentHashMap<>(2);

}
