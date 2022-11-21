package top.paakciu.proxy.core.protocal.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import top.paakciu.proxy.core.protocal.packet.base.BasePacket;

import java.util.List;

/**
 * @author paakciu
 * @ClassName: B2MPacketCodecHandler
 * @date: 2021/3/3 17:50
 */
//@Deprecated
@ChannelHandler.Sharable
public class B2MPacketCodecHandler extends ByteToMessageCodec<BasePacket> {
    //单例
    public static final B2MPacketCodecHandler INSTANCE = new B2MPacketCodecHandler();

    @Override
    protected void encode(ChannelHandlerContext ctx, BasePacket msg, ByteBuf out) throws Exception {
        PacketCodec.encode(out,msg);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(PacketCodec.decode(in));
    }
}
