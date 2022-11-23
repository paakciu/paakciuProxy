package top.paakciu.proxy.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import top.paakciu.proxy.server.ServerCache;
import top.paakciu.proxy.server.context.ServerContext;
import top.paakciu.proxy.server.service.SendToClientService;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @Classname WecomeChannelHandler
 * @Date 2022/9/29 23:35
 * @Created by paakciu
 */
@Slf4j
public class WelcomeChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {
    /**
     *
     */
    private SendToClientService sendToClientService = SendToClientService.INSTANCE;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        // 通知代理客户端
        Channel userChannel = ctx.channel();
        Channel channelToClient = ServerContext.getChannelToClient();
        if (channelToClient == null) {
            // 该端口还没有代理客户端
            ctx.channel().close();
        } else {
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);
//            String text = new String(bytes, StandardCharsets.UTF_8);
//            log.info("WelcomeChannelHandler.channelRead0 bytes={}",text);

            String uuid = (String) userChannel.attr(AttributeKey.valueOf("uuid")).get();
            sendToClientService.sendData(uuid,bytes);
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("WelcomeChannelHandler.channelActive channelToWelcome={}",ctx.channel());
        String uuid = UUID.randomUUID().toString();
        Channel userChannel = ctx.channel();
        userChannel.attr(AttributeKey.valueOf("uuid")).set(uuid);
        ServerContext.putWelcomeChannel(uuid,userChannel);
        Channel channelToClient = ServerContext.getChannelToClient();
        if (channelToClient == null) {
            // 该端口还没有代理客户端
            ctx.channel().close();
        } else {
            sendToClientService.sendToConnect(uuid);
        }

        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.info("WelcomeChannelHandler.exceptionCaught");
        Channel userChannel = ctx.channel();
        String uuid = (String) userChannel.attr(AttributeKey.valueOf("uuid")).get();
        sendToClientService.sendToDisonnect(uuid);
        // 当出现异常就关闭连接
        ctx.close();

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("WelcomeChannelHandler.channelInactive");
        Channel userChannel = ctx.channel();
        String uuid = (String) userChannel.attr(AttributeKey.valueOf("uuid")).get();
        sendToClientService.sendToDisonnect(uuid);
        super.channelInactive(ctx);
    }
}
