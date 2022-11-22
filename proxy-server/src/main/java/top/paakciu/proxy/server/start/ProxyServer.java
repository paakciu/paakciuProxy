package top.paakciu.proxy.server.start;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import top.paakciu.proxy.common.IMConfig;
import top.paakciu.proxy.core.protocal.codec.B2MPacketCodecHandler;
import top.paakciu.proxy.core.protocal.handler.HeartBeatTimerHandler;
import top.paakciu.proxy.core.protocal.handler.IdleDetectionHandler;
import top.paakciu.proxy.core.protocal.handler.PreFrameDecoder;
import top.paakciu.proxy.server.handler.ServerChannelHandler;
import top.paakciu.proxy.server.handler.WelcomeChannelHandler;

/**
 * @Classname ProxyServer
 * @Date 2022/11/22 18:16
 * @Created by paakciu
 */
@Slf4j
public class ProxyServer {

    int clientPort = 88;

    int welcomePort = 89;

    public void start(){
        NioEventLoopGroup serverBossGroup = new NioEventLoopGroup();
        NioEventLoopGroup serverWorkerGroup = new NioEventLoopGroup();

        //与客户端的主连接
        ServerBootstrap bootstrapToClient = new ServerBootstrap();
        bootstrapToClient.group(serverBossGroup, serverWorkerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new IdleDetectionHandler(IMConfig.SERVER_IDLE_TIME,IMConfig.SERVER_TIME_UNIT));
                ch.pipeline().addLast(new PreFrameDecoder());
                ch.pipeline().addLast(new B2MPacketCodecHandler());
//                ch.pipeline().addLast(new HeartBeatTimerHandler());
                ch.pipeline().addLast(new ServerChannelHandler());
            }
        });
        bootstrapToClient.bind(clientPort).addListener(future -> {
            if(future.isSuccess()){
                log.info("bootstrapToClient 绑定端口{}成功！",clientPort);
            }
        });

        //欢迎线程
        ServerBootstrap welcomeBootstrap = new ServerBootstrap();
        welcomeBootstrap.group(serverBossGroup, serverWorkerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new WelcomeChannelHandler());
            }
        });
        welcomeBootstrap.bind(welcomePort).addListener(future -> {
            if(future.isSuccess()){
                log.info("welcomeBootstrap 绑定端口{}成功！",welcomePort);
            }
        });
    }

    public static void main(String[] args) {
        ProxyServer server = new ProxyServer();
        server.start();
    }
}
