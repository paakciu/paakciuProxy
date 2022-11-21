package top.paakciu.proxy.client.service;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import top.paakciu.proxy.common.util.EmptyUtil;
import top.paakciu.proxy.core.protocal.packet.special.ProxyPacket;

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


}
