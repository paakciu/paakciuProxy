package top.paakciu.proxy.client.service;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import top.paakciu.proxy.client.ClientCache;
import top.paakciu.proxy.client.ClientContext;
import top.paakciu.proxy.common.util.EmptyUtil;

import java.nio.charset.StandardCharsets;

/**
 * @Classname LocalChannelManagerService
 * @Date 2022/11/7 22:02
 * @Created by paakciu
 */
@Slf4j
public class LocalChannelService {
    /**
     * 单例
     */
    public static final LocalChannelService INSTANCE = new LocalChannelService();

    /**
     * 连接本地连接
     */
    public void connectToLocal(String uuid){
        //本地启动的时候需要选择代理的端口
        String ip = "localhost";
        int port = 8080;

        Bootstrap localBootstrap = ClientContext.getLocalBootstrap();
        localBootstrap.connect(ip, port).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                // 连接后端服务器成功
                if (future.isSuccess()) {
                    final Channel localChannel = future.channel();
                    log.info("LocalChannelService connect localChannel success, uuid={},localChannel={}", uuid,localChannel);
                    ClientContext.putLocalChannel(uuid,localChannel);
                }
            }
        });
    }

    public void sendMessageToLocal(String uuid,byte[] data){
        if (EmptyUtil.isEmpty(uuid)){
            return ;
        }
        if (EmptyUtil.isEmpty(data)){
            return ;
        }
        Channel channelToLocal = ClientContext.getLocalChannel(uuid);
        if (channelToLocal != null) {

            ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(data.length);
            buf.writeBytes(data);
            //todo 可以删除
            String text = new String(data, StandardCharsets.UTF_8);
            log.info("LocalChannelService sendMessageToLocal data={}",text);
            channelToLocal.writeAndFlush(buf);
        }

    }

    public void disconnect(String uuid){
        if (EmptyUtil.isEmpty(uuid)){
            return ;
        }
        Channel channelToLocal = ClientContext.getLocalChannel(uuid);
        if (channelToLocal != null) {
            channelToLocal.disconnect();
        }
    }
}
