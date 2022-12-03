package top.paakciu.proxy.core.protocal.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import top.paakciu.proxy.common.IMConfig;

import java.util.concurrent.TimeUnit;

/**
 * @Classname IdleDetectionHandler
 * @Date 2022/11/8 23:39
 * @Created by paakciu
 */
public class IdleDetectionHandler  extends IdleStateHandler {

    long allIdleTime;

    public IdleDetectionHandler(long allIdleTime, TimeUnit unit) {
        super(0, allIdleTime, 0, unit);
        this.allIdleTime=allIdleTime;
    }

    public IdleDetectionHandler(long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit) {
        super(readerIdleTime, writerIdleTime, allIdleTime, unit);
        this.allIdleTime=allIdleTime;
    }


    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        System.out.println(allIdleTime + "秒内未读到数据，关闭连接");
        ctx.channel().close();
    }
}
