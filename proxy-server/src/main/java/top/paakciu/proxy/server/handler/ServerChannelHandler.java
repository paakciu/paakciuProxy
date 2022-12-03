package top.paakciu.proxy.server.handler;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import top.paakciu.proxy.common.IMConfig;
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
//        log.info("ServerChannelHandler.channelRead0 proxyPacket={}", JSON.toJSONString(proxyPacket));
        if(proxyPacket==null){
            return ;
        }
        try{
            byte type = proxyPacket.getType();
            String uuid = proxyPacket.getUuid();
            switch (type){
                case ProxyPacketType.DISCONNECT:
                    sendToWelcomeService.disconnect(uuid);
                    break;
                case ProxyPacketType.TRANSFER:
                    sendToWelcomeService.sendData(uuid, proxyPacket.getData());
                    break;
                case ProxyPacketType.CHECK_CONNECT:
                    byte[] data = proxyPacket.getData();
                    if(IMConfig.KEY.equals(new String(data,StandardCharsets.UTF_8))){
                        Channel channelToClient = ctx.channel();
                        log.info("确认绑定客户端连接！ channelToClient={}",channelToClient);
                        ServerContext.setChannelToClient(channelToClient);
                    }
                    break;
                default:
                    return;
            }
        }catch (Exception e){
            log.error("ServerChannelHandler.channelRead0 unknown error!",e);
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("ServerChannelHandler.channelActive channelToClient={}",ctx.channel());
        super.channelActive(ctx);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("ServerChannelHandler.channelInactive channelToClient={}",ctx.channel());
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("ServerChannelHandler.exception caught", cause);
        super.exceptionCaught(ctx, cause);
    }
}
