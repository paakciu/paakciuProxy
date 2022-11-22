package top.paakciu.proxy.server.context;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname ServerContext
 * @Date 2022/11/21 23:10
 * @Created by paakciu
 */
public class ServerContext {
    /**
     *
     */
    public static Map<String, Channel> channelToWelcomeMap = new ConcurrentHashMap<>(2);
    /**
     *
     */
    private static Channel channelToClient;

    public static Channel getChannelToClient() {
        return channelToClient;
    }

    public static void setChannelToClient(Channel channelToClient) {
        ServerContext.channelToClient = channelToClient;
    }

    public static void putWelcomeChannel(String uuid,Channel welcomeChannel){
        channelToWelcomeMap.put(uuid,welcomeChannel);
    }

    public static Channel getWelcomeChannel(String uuid){
        return channelToWelcomeMap.get(uuid);
    }
}
