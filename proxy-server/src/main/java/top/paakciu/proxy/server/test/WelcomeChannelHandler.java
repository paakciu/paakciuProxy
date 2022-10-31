package top.paakciu.proxy.server.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.paakciu.proxy.core.test.ProxyMessage;
import top.paakciu.proxy.server.ServerCache;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @Classname WecomeChannelHandler
 * @Date 2022/9/29 23:35
 * @Created by paakciu
 */
@Slf4j
public class WelcomeChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {

        // 通知代理客户端
        Channel userChannel = ctx.channel();
        Channel channelToClient = ServerCache.getChannelToClient();
        if (channelToClient == null) {
            // 该端口还没有代理客户端
            ctx.channel().close();
        } else {
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);
            String text = new String(bytes, StandardCharsets.UTF_8);
            log.info("WelcomeChannelHandler.channelRead0 bytes={}",text);
            ProxyMessage proxyMessage = new ProxyMessage();
            proxyMessage.setType(ProxyMessage.P_TYPE_TRANSFER);
            proxyMessage.setData(bytes);
            channelToClient.writeAndFlush(proxyMessage);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.info("WelcomeChannelHandler.exceptionCaught");
        // 当出现异常就关闭连接
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("WelcomeChannelHandler.channelActive");
        Channel userChannel = ctx.channel();
        ServerCache.setChannelToWelcome(userChannel);
//        InetSocketAddress sa = (InetSocketAddress) userChannel.localAddress();
        Channel channelToClient = ServerCache.getChannelToClient();
        if (channelToClient == null) {
            // 该端口还没有代理客户端
            ctx.channel().close();
        } else {

            ProxyMessage proxyMessage = new ProxyMessage();
            proxyMessage.setType(ProxyMessage.TYPE_CONNECT);
//            proxyMessage.setData(lanInfo.getBytes());
            channelToClient.writeAndFlush(proxyMessage);
        }

        super.channelActive(ctx);
    }
}
