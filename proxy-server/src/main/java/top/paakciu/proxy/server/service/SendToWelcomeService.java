package top.paakciu.proxy.server.service;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import top.paakciu.proxy.common.util.EmptyUtil;
import top.paakciu.proxy.core.protocal.packet.special.ProxyPacket;
import top.paakciu.proxy.server.ServerCache;
import top.paakciu.proxy.server.context.ServerContext;

import java.nio.charset.StandardCharsets;

/**
 * @Classname SendToWelcomeService
 * @Date 2022/11/22 16:31
 * @Created by paakciu
 */
@Slf4j
public class SendToWelcomeService {
    /**
     * 单例
     */
    public static final SendToWelcomeService INSTANCE = new SendToWelcomeService();

    /**
     * 给欢迎线程传输数据
     */
    public void sendData(String uuid,byte[] data){
        if(EmptyUtil.isEmpty(uuid)){
            log.error("SendToWelcomeService.sendData uuid is empty error");
            return;
        }
        if(EmptyUtil.isEmpty(data)){
            log.error("SendToWelcomeService.sendData data is empty error");
            return;
        }

        Channel channelToWelcome = ServerContext.getWelcomeChannel(uuid);
        if (channelToWelcome != null) {
            ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(data.length);
//            log.info("ServerChannelHandler.handleTransferMessage proxyMessage.data={}", new String(data, StandardCharsets.UTF_8));
            buf.writeBytes(data);
            channelToWelcome.writeAndFlush(buf);
        }
    }

    public void disconnect(String uuid){
        log.info("SendToWelcomeService.disconnect!");
        if (EmptyUtil.isEmpty(uuid)){
            return ;
        }
        Channel channelToWelcome = ServerContext.getWelcomeChannel(uuid);
        if (channelToWelcome != null) {
            channelToWelcome.disconnect();
        }
    }
}
