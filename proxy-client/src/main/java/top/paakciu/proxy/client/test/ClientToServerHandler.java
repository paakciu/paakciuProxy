package top.paakciu.proxy.client.test;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.paakciu.proxy.client.ClientCache;
import top.paakciu.proxy.core.test.ProxyMessage;

import java.nio.charset.StandardCharsets;

/**
 * @Classname ClientToServerHandler
 * @Date 2022/10/6 22:39
 * @Created by paakciu
 */
@Slf4j
public class ClientToServerHandler extends SimpleChannelInboundHandler<ProxyMessage> {
    private Bootstrap serverBootstrap;

    private Bootstrap localBootstrap;

    public ClientToServerHandler(Bootstrap localBootstrap,Bootstrap serverBootstrap){
        this.localBootstrap=localBootstrap;
        this.serverBootstrap=serverBootstrap;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("ClientToServerHandler.exception caught", cause);
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ClientCache.setChannelToServer(ctx.channel());
        log.info("ClientToServerHandler.channelActive channelToServer={}",ctx.channel());
        super.channelInactive(ctx);
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("ClientToServerHandler.channelInactive channelToServer={}",ctx.channel());
        super.channelInactive(ctx);
    }

    /**
     * 处理消息的事件
     * @param channelHandlerContext
     * @param proxyMessage
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ProxyMessage proxyMessage) throws Exception {
        log.info("ClientToServerHandler.channelRead0 ");
        switch (proxyMessage.getType()) {
            case ProxyMessage.TYPE_CONNECT:
                handleConnectMessage(channelHandlerContext, proxyMessage);
                break;
            case ProxyMessage.TYPE_DISCONNECT:
                handleDisconnectMessage(channelHandlerContext, proxyMessage);
                break;
            case ProxyMessage.P_TYPE_TRANSFER:
                handleTransferMessage(channelHandlerContext, proxyMessage);
                break;
            default:
                break;
        }
    }

    /**
     * 数据传输
     * @param ctx
     * @param proxyMessage
     */
    private void handleTransferMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        Channel channelToLocal = ClientCache.getChannelToLocal();
        if (channelToLocal != null) {
            ByteBuf buf = ctx.alloc().buffer(proxyMessage.getData().length);
            buf.writeBytes(proxyMessage.getData());
            String text = new String(proxyMessage.getData(), StandardCharsets.UTF_8);
            log.info("ClientToServerHandler handleTransferMessage proxyMessage.getData()={}",text);
            channelToLocal.writeAndFlush(buf);
        }
    }

    /**
     * 断开本地连接-请求
     * @param ctx
     * @param proxyMessage
     */
    private void handleDisconnectMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        Channel channelToLocal = ClientCache.getChannelToLocal();
        log.info("handleDisconnectMessage, {}", channelToLocal);
        if (channelToLocal != null) {

        }
    }

    /**
     * 建立本地连接-请求
     * @param ctx
     * @param proxyMessage
     */
    private void handleConnectMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        String ip = "localhost";
        int port = 8080;
        localBootstrap.connect(ip, port).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                // 连接后端服务器成功
                if (future.isSuccess()) {
                    final Channel localChannel = future.channel();
                    log.info("connect localChannel success, localChannel={}", localChannel);
                    ClientCache.setChannelToLocal(localChannel);
                }
            }
        });
    }
}
