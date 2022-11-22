package top.paakciu.proxy.server.service;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import top.paakciu.proxy.common.enums.ProxyPacketType;
import top.paakciu.proxy.common.util.EmptyUtil;
import top.paakciu.proxy.core.protocal.packet.special.ProxyPacket;
import top.paakciu.proxy.server.ServerCache;
import top.paakciu.proxy.server.context.ServerContext;

import java.util.UUID;

/**
 * @Classname SendToClientService
 * @Date 2022/11/21 22:39
 * @Created by paakciu
 */
@Slf4j
public class SendToClientService {
    /**
     * 单例
     */
    public final static SendToClientService INSTANCE = new SendToClientService();

    /**
     * 请求建立连接
     */
    public void sendToConnect(String uuid){
        Channel channelToClient = ServerContext.getChannelToClient();
        if(channelToClient==null){
            log.error("SendToClientService.sendToConnect channelToClient is empty! error");
            return;
        }
        if(EmptyUtil.isEmpty(uuid)){
            log.error("SendToClientService.sendToConnect uuid is empty error");
            return;
        }
        log.error("SendToClientService.sendToConnect uuid={}",uuid);
        ProxyPacket proxyPacket = new ProxyPacket();
        proxyPacket.setType(ProxyPacketType.CONNECT);
        proxyPacket.setUuid(uuid);
        channelToClient.writeAndFlush(proxyPacket);
    }

    /**
     * 请求断开连接
     */
    public void sendToDisonnect(String uuid){
        Channel channelToClient = ServerContext.getChannelToClient();
        if(channelToClient==null){
            log.error("SendToClientService.sendToDisonnect channelToClient is empty! error");
            return;
        }
        if(EmptyUtil.isEmpty(uuid)){
            log.error("SendToClientService.sendToDisonnect uuid is empty error");
            return;
        }

        ProxyPacket proxyPacket = new ProxyPacket();
        proxyPacket.setType(ProxyPacketType.DISCONNECT);
        proxyPacket.setUuid(uuid);
        channelToClient.writeAndFlush(proxyPacket);
    }

    /**
     * 给客户端传输数据
     */
    public void sendData(String uuid,byte[] data){
        Channel channelToClient = ServerContext.getChannelToClient();
        if(channelToClient==null){
            log.error("SendToClientService.sendData channelToClient is empty! error");
            return;
        }
        if(EmptyUtil.isEmpty(uuid)){
            log.error("SendToClientService.sendData uuid is empty error");
            return;
        }
        if(EmptyUtil.isEmpty(data)){
            log.error("SendToClientService.sendData data is empty error");
            return;
        }
        ProxyPacket proxyPacket = new ProxyPacket();
        proxyPacket.setType(ProxyPacketType.TRANSFER);
        proxyPacket.setUuid(uuid);
        proxyPacket.setData(data);
        channelToClient.writeAndFlush(proxyPacket);
    }
}
