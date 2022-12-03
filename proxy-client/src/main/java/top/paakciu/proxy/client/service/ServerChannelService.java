package top.paakciu.proxy.client.service;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import top.paakciu.proxy.client.ClientContext;
import top.paakciu.proxy.common.IMConfig;
import top.paakciu.proxy.common.enums.ProxyPacketType;
import top.paakciu.proxy.common.util.EmptyUtil;
import top.paakciu.proxy.core.protocal.packet.special.ProxyPacket;

import java.nio.charset.StandardCharsets;

/**
 * @Classname ServerChannelManagerService
 * @Date 2022/11/9 22:22
 * @Created by paakciu
 */
@Slf4j
public class ServerChannelService {
    /**
     * 单例-服务端连接
     */
    public static final ServerChannelService INSTANCE = new ServerChannelService();
    /**
     * 依赖-客户端连接
     */
   private LocalChannelService localChannelService = LocalChannelService.INSTANCE;

    /**
     * 建立本地连接-请求
     * @param ctx
     * @param proxyPacket
     */
    public void connect(ChannelHandlerContext ctx,  ProxyPacket proxyPacket) {
        log.info("ServerChannelService.connect!");
        String uuid = proxyPacket.getUuid();
        if(EmptyUtil.isEmpty(uuid)){
            return ;
        }
        localChannelService.connectToLocal(uuid);
    }

    /**
     * 数据传输
     * @param ctx
     * @param proxyPacket
     */
    public void transfer(ChannelHandlerContext ctx, ProxyPacket proxyPacket) {
        String uuid = proxyPacket.getUuid();
        byte[] data = proxyPacket.getData();
        if (EmptyUtil.isEmpty(uuid)){
            return ;
        }
        if (EmptyUtil.isEmpty(data)){
            return ;
        }
        localChannelService.sendMessageToLocal(uuid,data);
    }

    /**
     * 断开本地连接-请求
     * @param ctx
     * @param proxyPacket
     */
    public void disconnect(ChannelHandlerContext ctx,  ProxyPacket proxyPacket) {
        String uuid = proxyPacket.getUuid();
        if (EmptyUtil.isEmpty(uuid)){
            return ;
        }
        localChannelService.disconnect(uuid);
    }

    public void checkConnection(){
        Channel channelToServer = ClientContext.getChannelToServer();
        String key = IMConfig.KEY;
        ProxyPacket proxyPacket = new ProxyPacket();
        proxyPacket.setType(ProxyPacketType.CHECK_CONNECT);
        proxyPacket.setData(key.getBytes(StandardCharsets.UTF_8));
        log.info("对主连接发送确认连接数据包 packet={} ,channelToServer={}", JSON.toJSONString(proxyPacket),channelToServer);
        channelToServer.writeAndFlush(proxyPacket);
    }
}
