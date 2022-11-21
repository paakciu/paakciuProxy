package top.paakciu.proxy.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import lombok.Data;
import top.paakciu.proxy.core.protocal.packet.special.ProxyPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @Classname ClientCache
 * @Date 2022/9/25 22:27
 * @Created by paakciu
 */
public class ClientCache {
//    private static volatile Channel channelToLocal;
//
//    private static volatile Channel channelToServer;
//
////    public static BlockingQueue<ProxyPacket> SEND_QUEUE = new ArrayBlockingQueue(100);
//
//    public static Map<String,Channel> channelToLocalMap = new HashMap<>(2);
//    public static Channel getChannelToLocal() {
//        return channelToLocal;
//    }
//
//    public static void setChannelToLocal(Channel channelToLocal) {
//        ClientCache.channelToLocal = channelToLocal;
//    }
//
//    public static Channel getChannelToServer() {
//        return channelToServer;
//    }
//
//    public static void setChannelToServer(Channel channelToServer) {
//        ClientCache.channelToServer = channelToServer;
//    }
//
//    public static Map<String, Channel> getChannelToLocalMap() {
//        return channelToLocalMap;
//    }
//
//    public static void setChannelToLocalMap(Map<String, Channel> channelToLocalMap) {
//        ClientCache.channelToLocalMap = channelToLocalMap;
//    }
}
