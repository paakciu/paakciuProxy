package top.paakciu.proxy.client;

import io.netty.channel.Channel;

/**
 * @Classname ClientCache
 * @Date 2022/9/25 22:27
 * @Created by paakciu
 */
public class ClientCache {
    public static volatile Channel channelToLocal;

    public static volatile Channel channelToServer;

    public static Channel getChannelToLocal() {
        return channelToLocal;
    }

    public static void setChannelToLocal(Channel channelToLocal) {
        ClientCache.channelToLocal = channelToLocal;
    }

    public static Channel getChannelToServer() {
        return channelToServer;
    }

    public static void setChannelToServer(Channel channelToServer) {
        ClientCache.channelToServer = channelToServer;
    }
}
