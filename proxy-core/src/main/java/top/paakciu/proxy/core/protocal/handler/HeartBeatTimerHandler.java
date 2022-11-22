package top.paakciu.proxy.core.protocal.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import top.paakciu.proxy.common.IMConfig;
import top.paakciu.proxy.core.protocal.packet.base.HeartBeatPacket;

import java.util.concurrent.TimeUnit;

/**
 * @author paakciu
 * @ClassName: HeartBeatTimerHandler
 * @date: 2021/3/31 18:55
 */
@Slf4j
public class HeartBeatTimerHandler extends ChannelInboundHandlerAdapter {
    private static final int HEARTBEAT_INTERVAL = IMConfig.CLIENT_HEARTBEAT_INTERVAL;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("HeartBeatTimerHandler 心跳发包处理器准备就绪");
        scheduleSendHeartBeat(ctx);
        super.channelActive(ctx);
    }

    private void scheduleSendHeartBeat(ChannelHandlerContext ctx) {
        ctx.executor().schedule(() -> {
            if (ctx.channel().isActive()) {
                log.info("HeartBeatTimerHandler.scheduleSendHeartBeat");
                ctx.writeAndFlush(new HeartBeatPacket());
                scheduleSendHeartBeat(ctx);
            }
        }, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }

}
