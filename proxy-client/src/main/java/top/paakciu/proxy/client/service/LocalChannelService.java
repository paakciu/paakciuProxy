package top.paakciu.proxy.client.service;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import top.paakciu.proxy.client.ClientCache;
import top.paakciu.proxy.client.ClientContext;
import top.paakciu.proxy.common.util.EmptyUtil;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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

        log.info("LocalChannelService.connectToLocal!");
        ClientCache.packetStateCache.put(uuid,true);
        ClientCache.packetCache.put(uuid,new ArrayList<>());
        Bootstrap localBootstrap = ClientContext.getLocalBootstrap();
        localBootstrap.connect(ip, port).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                // 连接后端服务器成功
                if (future.isSuccess()) {
                    final Channel localChannel = future.channel();
                    localChannel.attr(AttributeKey.valueOf("uuid")).set(uuid);
                    log.info("LocalChannelService connect localChannel success, uuid={},localChannel={}", uuid,localChannel);
                    ClientContext.putLocalChannel(uuid,localChannel);
                    ClientCache.packetStateCache.put(uuid,false);
                    CachePop(uuid);
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
        Boolean state = ClientCache.packetStateCache.getOrDefault(uuid, false);
        if(state){
            List<byte[]> list = ClientCache.packetCache.get(uuid);
            list.add(data);
            log.info("LocalChannelService sendMessageToLocal cache message uuid={}",uuid);
            return;
        }
        Channel channelToLocal = ClientContext.getLocalChannel(uuid);
        if (channelToLocal != null) {
            ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(data.length);
            buf.writeBytes(data);
            //todo 可以删除
            String text = new String(data, StandardCharsets.UTF_8);
            log.info("LocalChannelService sendMessageToLocal data={},uuid={}",text,uuid);
            channelToLocal.writeAndFlush(buf);
        }else{
            log.info("LocalChannelService sendMessageToLocal channelToLocal is empty! uuid={}",uuid);
        }
    }

    private void CachePop(String uuid){
        List<byte[]> list = ClientCache.packetCache.get(uuid);
        Channel channelToLocal = ClientContext.getLocalChannel(uuid);
        if (channelToLocal != null) {
            for (byte[] data : list) {
                ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(data.length);
                buf.writeBytes(data);
                //todo 可以删除
                String text = new String(data, StandardCharsets.UTF_8);
                log.info("LocalChannelService CachePop data={},uuid={}",text,uuid);
                channelToLocal.writeAndFlush(buf);
            }
        }else{
            log.info("LocalChannelService CachePop channelToLocal is empty! uuid={}",uuid);
        }
        ClientCache.packetCache.remove(uuid);
    }


    public void disconnect(String uuid){
        log.info("LocalChannelService.disconnect!");
        if (EmptyUtil.isEmpty(uuid)){
            return ;
        }
        Channel channelToLocal = ClientContext.getLocalChannel(uuid);
        if (channelToLocal != null) {
            channelToLocal.disconnect();
        }
    }
}
