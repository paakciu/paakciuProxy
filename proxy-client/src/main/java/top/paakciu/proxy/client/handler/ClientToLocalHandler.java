package top.paakciu.proxy.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import top.paakciu.proxy.client.ClientContext;
import top.paakciu.proxy.common.enums.ProxyPacketType;
import top.paakciu.proxy.core.protocal.packet.special.ProxyPacket;
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
//        log.info("ClientToLocalHandler.channelRead0 enter");
        Channel channelToLocal = ctx.channel();
        Channel channelToServer = ClientContext.getChannelToServer();
        if (channelToServer == null) {
            log.info("ClientToLocalHandler.channelRead0 channelToServer is null");
            ctx.channel().close();
        } else {
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            String text = new String(bytes, StandardCharsets.UTF_8);
            log.info("ClientToLocalHandler.channelRead0 text={}",text);

            String uuid = (String) channelToLocal.attr(AttributeKey.valueOf("uuid")).get();
            ProxyPacket proxyPacket = new ProxyPacket();
            proxyPacket.setType(ProxyPacketType.TRANSFER);
            proxyPacket.setData(bytes);
            proxyPacket.setUuid(uuid);
            channelToServer.writeAndFlush(proxyPacket);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("ClientToLocalHandler.channelActive");
//        Channel channelToLocal = ctx.channel();
//        ClientCache.setChannelToLocal(channelToLocal);
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
