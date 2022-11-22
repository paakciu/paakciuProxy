package top.paakciu.proxy.server.handler;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import top.paakciu.proxy.common.enums.ProxyPacketType;
import top.paakciu.proxy.core.protocal.packet.special.ProxyPacket;
import top.paakciu.proxy.server.ServerCache;
import top.paakciu.proxy.server.context.ServerContext;
import top.paakciu.proxy.server.service.SendToWelcomeService;

import java.nio.charset.StandardCharsets;

/**
 * @Classname ServerChannelHandler
 * @Date 2022/11/22 16:26
 * @Created by paakciu
 */
@Slf4j
public class ServerChannelHandler extends SimpleChannelInboundHandler<ProxyPacket> {

    /**
     *
     */
    SendToWelcomeService sendToWelcomeService = SendToWelcomeService.INSTANCE;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProxyPacket proxyPacket) throws Exception {
        log.info("ServerChannelHandler.channelRead0 proxyPacket={}", JSON.toJSONString(proxyPacket));
        if(proxyPacket==null){
            return ;
        }
        byte type = proxyPacket.getType();
        String uuid = proxyPacket.getUuid();
        switch (type){
            case ProxyPacketType.DISCONNECT:
                sendToWelcomeService.disconnect(uuid);
                break;
            case ProxyPacketType.TRANSFER:
                sendToWelcomeService.sendData(uuid, proxyPacket.getData());
                break;
            default:
                return;
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("ServerChannelHandler.channelActive channelToClient={}",ctx.channel());
        Channel channelToClient = ctx.channel();
        ServerContext.setChannelToClient(channelToClient);
        super.channelActive(ctx);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("ServerChannelHandler.channelInactive channelToServer={}",ctx.channel());
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("ServerChannelHandler.exception caught", cause);
        super.exceptionCaught(ctx, cause);
    }
}
