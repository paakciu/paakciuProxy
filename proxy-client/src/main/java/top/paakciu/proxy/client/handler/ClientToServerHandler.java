package top.paakciu.proxy.client.handler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import top.paakciu.proxy.client.ClientCache;
import top.paakciu.proxy.client.ClientContext;
import top.paakciu.proxy.client.service.ServerChannelService;
import top.paakciu.proxy.core.protocal.packet.special.ProxyPacket;

/**
 * @Classname ClientToServerHandler
 * @Date 2022/11/9 22:10
 * @Created by paakciu
 */
@Slf4j
public class ClientToServerHandler extends SimpleChannelInboundHandler<ProxyPacket> {

    /**
     * 业务
     */
    ServerChannelService serverChannelService = ServerChannelService.INSTANCE;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProxyPacket proxyPacket) throws Exception {
        log.info("ClientToServerHandler.channelRead0 proxyPacket={}", JSON.toJSONString(proxyPacket));
        if(proxyPacket==null){
            return ;
        }
        byte type = proxyPacket.getType();
        switch (type){
            case ProxyPacket.ProxyPacketType.CONNECT:
                serverChannelService.connect(ctx,proxyPacket);
                break;
            case ProxyPacket.ProxyPacketType.DISCONNECT:
                serverChannelService.disconnect(ctx,proxyPacket);
                break;
            case ProxyPacket.ProxyPacketType.TRANSFER:
                serverChannelService.transfer(ctx,proxyPacket);
                break;
            default:
                return;
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("ClientToServerHandler.channelActive channelToServer={}",ctx.channel());
        Channel channelToServer = ctx.channel();
        ClientContext.setChannelToServer(channelToServer);
        super.channelInactive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("ClientToServerHandler.channelInactive channelToServer={}",ctx.channel());
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("ClientToServerHandler.exception caught", cause);
        super.exceptionCaught(ctx, cause);
    }

}
