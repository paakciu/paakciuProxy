package top.paakciu.proxy.client.handler;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import top.paakciu.proxy.client.ClientCache;
import top.paakciu.proxy.client.ClientContext;
import top.paakciu.proxy.client.service.ServerChannelService;
import top.paakciu.proxy.common.enums.ProxyPacketType;
import top.paakciu.proxy.core.protocal.packet.special.ProxyPacket;

import java.util.concurrent.TimeUnit;

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
//        log.info("ClientToServerHandler.channelRead0 proxyPacket={}", JSON.toJSONString(proxyPacket));
        if(proxyPacket==null){
            return ;
        }
        byte type = proxyPacket.getType();
        switch (type){
            case ProxyPacketType.CONNECT:
                serverChannelService.connect(ctx,proxyPacket);
                break;
            case ProxyPacketType.DISCONNECT:
                serverChannelService.disconnect(ctx,proxyPacket);
                break;
            case ProxyPacketType.TRANSFER:
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
        serverChannelService.checkConnection();
        super.channelInactive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("ClientToServerHandler.channelInactive channelToServer={}",ctx.channel());
        log.error("掉线了...");
        //使用过程中断线重连
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(() -> {
            Bootstrap serverBootstrap = ClientContext.getServerBootstrap();
            log.info("重新连接-》绑定地址端口{}:{}",ClientCache.ServerIp,ClientCache.ServerPort);
            serverBootstrap.connect(ClientCache.ServerIp,ClientCache.ServerPort).addListener(future -> {
                if(future.isSuccess()){
                    log.info("客户端主连接重新建立完成！");
                }else{
                    log.error("客户端主连接重连失败，正在尝试重连");
                }
            });
        }, 1L, TimeUnit.SECONDS);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("ClientToServerHandler.exception caught", cause);
        super.exceptionCaught(ctx, cause);
    }

}
