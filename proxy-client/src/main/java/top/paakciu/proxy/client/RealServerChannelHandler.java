//package top.paakciu.proxy.client;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.SimpleChannelInboundHandler;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * 处理服务端 channel.
// */
//public class RealServerChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {
//
//    private static Logger logger = LoggerFactory.getLogger(RealServerChannelHandler.class);
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
//        Channel realServerChannel = ctx.channel();
//
//        Channel channel = ClientCache.channelToServer;
//        if (channel == null) {
//            // 代理客户端连接断开
//            ctx.channel().close();
//        } else {
//            byte[] bytes = new byte[buf.readableBytes()];
//            buf.readBytes(bytes);
//            String userId = ClientChannelMannager.getRealServerChannelUserId(realServerChannel);
//            ProxyMessage proxyMessage = new ProxyMessage();
//            proxyMessage.setType(ProxyMessage.P_TYPE_TRANSFER);
//            proxyMessage.setUri(userId);
//            proxyMessage.setData(bytes);
//            channel.writeAndFlush(proxyMessage);
//            logger.debug("write data to proxy server, {}, {}", realServerChannel, channel);
//        }
//    }
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        super.channelActive(ctx);
//    }
//
//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        Channel realServerChannel = ctx.channel();
//        String userId = ClientChannelMannager.getRealServerChannelUserId(realServerChannel);
//        ClientChannelMannager.removeRealServerChannel(userId);
//        Channel channel = ClientCache.channelToServer;
//        if (channel != null) {
//            logger.debug("channelInactive, {}", realServerChannel);
//            ProxyMessage proxyMessage = new ProxyMessage();
//            proxyMessage.setType(ProxyMessage.TYPE_DISCONNECT);
//            proxyMessage.setUri(userId);
//            channel.writeAndFlush(proxyMessage);
//        }
//
//        super.channelInactive(ctx);
//    }
//
//    @Override
//    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
//        Channel realServerChannel = ctx.channel();
//        Channel proxyChannel = ClientCache.channelToServer;
//        if (proxyChannel != null) {
//            proxyChannel.config().setOption(ChannelOption.AUTO_READ, realServerChannel.isWritable());
//        }
//
//        super.channelWritabilityChanged(ctx);
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        logger.error("exception caught", cause);
//        super.exceptionCaught(ctx, cause);
//    }
//}