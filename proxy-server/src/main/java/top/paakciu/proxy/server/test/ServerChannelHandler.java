package top.paakciu.proxy.server.test;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.paakciu.proxy.core.test.ProxyMessage;
import top.paakciu.proxy.server.ServerCache;

import java.nio.charset.StandardCharsets;

/**
 * @Classname ServerChannelHandler
 * @Date 2022/9/29 23:29
 * @Created by paakciu
 */
@Slf4j
public class ServerChannelHandler extends SimpleChannelInboundHandler<ProxyMessage> {
    private static Logger logger = LoggerFactory.getLogger(ServerChannelHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProxyMessage proxyMessage) throws Exception {
        switch (proxyMessage.getType()) {
            case ProxyMessage.TYPE_CONNECT:
                handleConnectMessage(ctx, proxyMessage);
                break;

            case ProxyMessage.P_TYPE_TRANSFER:
                handleTransferMessage(ctx, proxyMessage);
                break;
            default:
                break;
        }
    }
    private void handleConnectMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        log.info("ServerChannelHandler.handleConnectMessage proxyMessage={}", JSON.toJSONString(proxyMessage));
    }

    private void handleTransferMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        Channel channelToWelcome = ServerCache.getChannelToWelcome();
        if (channelToWelcome != null) {
            ByteBuf buf = ctx.alloc().buffer(proxyMessage.getData().length);
            log.info("ServerChannelHandler.handleTransferMessage proxyMessage.data={}",new String(proxyMessage.getData(), StandardCharsets.UTF_8));
            buf.writeBytes(proxyMessage.getData());
            channelToWelcome.writeAndFlush(buf);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("ServerChannelHandler.channelActive");
        Channel channelToClient = ctx.channel();
        ServerCache.setChannelToClient(channelToClient);
        super.channelActive(ctx);
    }
}
