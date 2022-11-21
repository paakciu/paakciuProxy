package top.paakciu.proxy.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname ClientContext
 * @Date 2022/11/9 22:07
 * @Created by paakciu
 */
public class ClientContext {

    private static volatile Bootstrap localBootstrap;

    private static volatile Bootstrap serverBootstrap;

    private static volatile Channel channelToServer;

    public static Map<String, Channel> channelToLocalMap = new ConcurrentHashMap<>(2);
    public static Bootstrap getLocalBootstrap() {
        return localBootstrap;
    }

    public static void setLocalBootstrap(Bootstrap localBootstrap) {
        ClientContext.localBootstrap = localBootstrap;
    }

    public static Bootstrap getServerBootstrap() {
        return serverBootstrap;
    }

    public static void setServerBootstrap(Bootstrap serverBootstrap) {
        ClientContext.serverBootstrap = serverBootstrap;
    }

    public static Channel getChannelToServer() {
        return channelToServer;
    }

    public static void setChannelToServer(Channel channelToServer) {
        ClientContext.channelToServer = channelToServer;
    }

    public static void putLocalChannel(String uuid,Channel localChannel){
        channelToLocalMap.put(uuid,localChannel);
    }

    public static Channel getLocalChannel(String uuid){
        return channelToLocalMap.get(uuid);
    }
}
