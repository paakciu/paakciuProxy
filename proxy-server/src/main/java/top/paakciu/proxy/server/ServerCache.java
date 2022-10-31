package top.paakciu.proxy.server;

import io.netty.channel.Channel;

/**
 * @Classname ServerCache
 * @Date 2022/9/29 23:30
 * @Created by paakciu
 */
public class ServerCache {

    private static Channel channelToClient;

    private static Channel channelToWelcome;

    public static Channel getChannelToClient() {
        return channelToClient;
    }

    public static void setChannelToClient(Channel channelToClient) {
        ServerCache.channelToClient = channelToClient;
    }

    public static Channel getChannelToWelcome() {
        return channelToWelcome;
    }

    public static void setChannelToWelcome(Channel channelToWelcome) {
        ServerCache.channelToWelcome = channelToWelcome;
    }


}
