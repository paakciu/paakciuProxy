package top.paakciu.proxy.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import top.paakciu.proxy.client.test.ClientToServerHandler;
import top.paakciu.proxy.client.test.ClientToLocalHandler;
import top.paakciu.proxy.core.test.IdleCheckHandler;
import top.paakciu.proxy.core.test.ProxyMessageDecoder;
import top.paakciu.proxy.core.test.ProxyMessageEncoder;

import java.util.logging.Logger;

/**
 * @Classname SimpleClient
 * @Date 2022/10/6 22:40
 * @Created by paakciu
 */
@Slf4j
public class SimpleClient {
    private static final int MAX_FRAME_LENGTH = 1024 * 1024;

    private static final int LENGTH_FIELD_OFFSET = 0;

    private static final int LENGTH_FIELD_LENGTH = 4;

    private static final int INITIAL_BYTES_TO_STRIP = 0;

    private static final int LENGTH_ADJUSTMENT = 0;

    public void start(){
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap localBootstrap = new Bootstrap();
        localBootstrap.group(workerGroup);
        localBootstrap.channel(NioSocketChannel.class);
        localBootstrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ClientToLocalHandler());
            }
        });

        Bootstrap serverBootstrap = new Bootstrap();
        serverBootstrap.group(workerGroup);
        serverBootstrap.channel(NioSocketChannel.class);
        serverBootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ProxyMessageDecoder(MAX_FRAME_LENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH, LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP));
                ch.pipeline().addLast(new ProxyMessageEncoder());
                ch.pipeline().addLast(new IdleCheckHandler(IdleCheckHandler.READ_IDLE_TIME, IdleCheckHandler.WRITE_IDLE_TIME - 10, 0));
                ch.pipeline().addLast(new ClientToServerHandler(localBootstrap, serverBootstrap));
            }
        });

        String ip="localhost";
        int port=4396;
        serverBootstrap.connect(ip,port).addListener(future -> {
            if(future.isSuccess()){
                log.info("客户端主连接建立完成");
            }
        });
    }

    public static void main(String[] args) {
        SimpleClient simpleClient = new SimpleClient();
        simpleClient.start();
    }
}
