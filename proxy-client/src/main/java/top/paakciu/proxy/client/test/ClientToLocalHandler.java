package top.paakciu.proxy.client.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.paakciu.proxy.client.ClientCache;
import top.paakciu.proxy.core.test.ProxyMessage;

import java.nio.charset.StandardCharsets;

/**
 * @Classname ClientTolocalHandler
 * @Date 2022/10/6 22:39
 * @Created by paakciu
 */
@Slf4j
public class ClientToLocalHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        log.info("ClientToLocalHandler.channelRead0 enter");
        Channel channelToLocal = ctx.channel();
        Channel channelToServer = ClientCache.getChannelToServer();
        if (channelToServer == null) {
            log.info("ClientToLocalHandler.channelRead0 channelToServer is null");
            ctx.channel().close();
        } else {
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            String text = new String(bytes, StandardCharsets.UTF_8);
            log.info("ClientToLocalHandler.channelRead0 text={}",text);
            ProxyMessage proxyMessage = new ProxyMessage();
            proxyMessage.setType(ProxyMessage.P_TYPE_TRANSFER);
            proxyMessage.setData(bytes);
            channelToServer.writeAndFlush(proxyMessage);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("ClientToLocalHandler.channelActive");
        Channel channelToLocal = ctx.channel();
        ClientCache.setChannelToLocal(channelToLocal);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("ClientToLocalHandler.channelInactive");
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("ClientToLocalHandler.exception caught", cause);
        super.exceptionCaught(ctx, cause);
    }
}
