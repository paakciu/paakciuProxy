package top.paakciu.proxy.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.paakciu.proxy.core.test.IdleCheckHandler;
import top.paakciu.proxy.core.test.ProxyMessageDecoder;
import top.paakciu.proxy.core.test.ProxyMessageEncoder;
import top.paakciu.proxy.server.test.ServerChannelHandler;
import top.paakciu.proxy.server.test.WelcomeChannelHandler;

import java.util.concurrent.ExecutionException;

/**
 * @Classname BootServer
 * @Date 2022/9/29 23:24
 * @Created by paakciu
 */
public class BootServer {
    /**
     * max packet is 2M.
     */
    private static final int MAX_FRAME_LENGTH = 2 * 1024 * 1024;

    private static final int LENGTH_FIELD_OFFSET = 0;

    private static final int LENGTH_FIELD_LENGTH = 4;

    private static final int INITIAL_BYTES_TO_STRIP = 0;

    private static final int LENGTH_ADJUSTMENT = 0;

    private static Logger logger = LoggerFactory.getLogger(BootServer.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        BootServer server=new BootServer();
        server.start();
    }

    public void start() throws ExecutionException, InterruptedException {
        NioEventLoopGroup serverBossGroup = new NioEventLoopGroup();
        NioEventLoopGroup serverWorkerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrapToClient = new ServerBootstrap();
        bootstrapToClient.group(serverBossGroup, serverWorkerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ProxyMessageDecoder(MAX_FRAME_LENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH, LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP));
                ch.pipeline().addLast(new ProxyMessageEncoder());
                ch.pipeline().addLast(new IdleCheckHandler(IdleCheckHandler.READ_IDLE_TIME, IdleCheckHandler.WRITE_IDLE_TIME, 0));
                ch.pipeline().addLast(new ServerChannelHandler());
            }
        });
        bootstrapToClient.bind(4396).get();

        ServerBootstrap welcomeBootstrap = new ServerBootstrap();
        welcomeBootstrap.group(serverBossGroup, serverWorkerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new WelcomeChannelHandler());
            }
        });
        welcomeBootstrap.bind(8090).get();
    }
}
