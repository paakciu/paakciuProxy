package top.paakciu.proxy.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import top.paakciu.proxy.common.IMConfig;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @Classname Client
 * @Date 2022/9/25 22:15
 * @Created by paakciu
 */
public class Client {
    private NioEventLoopGroup workerGroup;

    private Bootstrap bootstrap;

    private Bootstrap realServerBootstrap;

    private static final int MAX_RETRY = IMConfig.ClientConnectionRetry;

    public static void main(String[] args) {


    }
    public void start(){
        int port=4396;
        workerGroup = new NioEventLoopGroup();

        realServerBootstrap = new Bootstrap();
        realServerBootstrap.group(workerGroup);
        realServerBootstrap.channel(NioSocketChannel.class);
        realServerBootstrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(SocketChannel ch) throws Exception {
//                ch.pipeline().addLast(new RealServerChannelHandler());
            }
        });
        connect(realServerBootstrap,"localhost",25536,3,1);
    }

    //建立连接 随机退避算法
    private void connect (Bootstrap bootstrap,String host,int port,int retry,int type)
    {
        //操作表,为了让代码看起来更加扁平,action存起来false 跟 true 对应的操作
        Map<Boolean, Consumer<Future<?>>> action=new HashMap<>();

        //连接成功
        action.put(true,future->{
            Channel channel = ((ChannelFuture) future).channel();
            if(1==type)
            {
                ClientCache.channelToLocal=channel;
            }else if(2==type){
                ClientCache.channelToServer=channel;
            }
            //todo,这里应当使用线程，使操作变成异步，以免阻塞欢迎连接
            System.out.println("连接成功！");
        });
        //连接失败
        action.put(false,future->{
            //随机退避算法
            //todo,这里应当使用线程，使操作变成异步，以免阻塞欢迎连接
            System.out.println("连接失败！正在重试");

            if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
                return;
            }
            // 第几次重连
            int order = (MAX_RETRY - retry) + 1;
            // 此次重连的间隔时间
            int delay = 1 << order;
            System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
            //使用计划任务来实现退避重连算法
            bootstrap.config().group().schedule(
                    () -> connect(bootstrap, host, port, retry - 1,type)
                    ,delay
                    ,TimeUnit.SECONDS
            );
        });

        //bootstrap开始连接,使用异步线程,结果返回为future.isSuccess()
        bootstrap.connect(host, port)
                .addListener(future -> {
                    action.get(future.isSuccess()).accept(future);
                });
    }
}
