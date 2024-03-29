package top.paakciu.proxy.client.start;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import top.paakciu.proxy.client.ClientCache;
import top.paakciu.proxy.client.ClientContext;
import top.paakciu.proxy.client.handler.ClientToLocalHandler;
import top.paakciu.proxy.client.handler.ClientToServerHandler;
import top.paakciu.proxy.common.IMConfig;
import top.paakciu.proxy.core.protocal.codec.B2MPacketCodecHandler;
import top.paakciu.proxy.core.protocal.handler.HeartBeatTimerHandler;
import top.paakciu.proxy.core.protocal.handler.IdleDetectionHandler;
import top.paakciu.proxy.core.protocal.handler.PreFrameDecoder;

import java.util.Scanner;

/**
 * @Classname proxyClient
 * @Date 2022/11/7 22:47
 * @Created by paakciu
 */
@Slf4j
public class ProxyClient {

    public void start(){
        //本地连接
        Bootstrap localBootstrap = new Bootstrap();
        localBootstrap.group(new NioEventLoopGroup());
        localBootstrap.channel(NioSocketChannel.class);
        localBootstrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ClientToLocalHandler());
            }
        });
        //服务端连接
        Bootstrap serverBootstrap = new Bootstrap();
        serverBootstrap.group(new NioEventLoopGroup());
        serverBootstrap.channel(NioSocketChannel.class);
        serverBootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
                ch.pipeline().addLast(new IdleDetectionHandler(IMConfig.CLIENT_IDLE_TIME,IMConfig.CLIENT_TIME_UNIT));
                ch.pipeline().addLast(new PreFrameDecoder());
                ch.pipeline().addLast(new B2MPacketCodecHandler());
                ch.pipeline().addLast(new HeartBeatTimerHandler());
                ch.pipeline().addLast(new ClientToServerHandler());


            }
        });

        ClientContext.setLocalBootstrap(localBootstrap);
        ClientContext.setServerBootstrap(serverBootstrap);

        log.info("绑定地址端口{}:{}",ClientCache.ServerIp,ClientCache.ServerPort);
        serverBootstrap.connect(ClientCache.ServerIp,ClientCache.ServerPort).addListener(future -> {
            if(future.isSuccess()){
                //经过测试 先会执行 handler的 channelActive 才会到这个成功，故断线重连的逻辑可以写进handler里，无需在这里体现
                log.info("客户端主连接建立完成！");
            }
        });
    }



    public static void main(String[] args) {
        //todo paakciu 需要输入 代理的端口 服务器ip+端口

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入代理服务器地址");
        ClientCache.ServerIp = scanner.nextLine();
        System.out.println("请输入代理服务器端口");
        ClientCache.ServerPort = scanner.nextInt();
        System.out.println("请输入本地端口");
        ClientCache.LocalPort = scanner.nextInt();

        ProxyClient client = new ProxyClient();
        client.start();
    }
}
//    /**
//     * 开启发送线程
//     * @param channelFuture
//     */
//    private void startClientSendJob(ChannelFuture channelFuture) {
//        Thread asyncSendJob = new Thread(new AsyncSendJob(channelFuture));
//        channelFuture.addListener(future -> {
//            if(future.isSuccess()){
//                asyncSendJob.start();
//            }
//        });
//
//    }
//
//    @Data
//    @AllArgsConstructor
//    class AsyncSendJob implements Runnable {
//        private ChannelFuture channelFuture;
//
//        @Override
//        public void run() {
//            while (true) {
//                try {
//                    //阻塞模式
//                    ProxyPacket data = ClientCache.SEND_QUEUE.take();
//                    System.out.println("发送data="+ JSON.toJSONString(data));
//                    channelFuture.channel().writeAndFlush(data);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }