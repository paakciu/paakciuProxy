package top.paakciu.proxy.core.protocal.handler;

/**
 * Netty 自带的拆包器有：
 * 1. 固定长度的拆包器 FixedLengthFrameDecoder
 * 如果你的应用层协议非常简单，每个数据包的长度都是固定的，
 * 比如 100，那么只需要把这个拆包器加到 pipeline 中，
 * Netty 会把一个个长度为 100 的数据包 (ByteBuf) 传递到下一个 channelHandler。
 *
 * 2. 行拆包器 LineBasedFrameDecoder
 * 从字面意思来看，发送端发送数据包的时候，
 * 每个数据包之间以换行符作为分隔，
 * 接收端通过 LineBasedFrameDecoder 将粘过的 ByteBuf 拆分成一个个完整的应用层数据包。
 *
 * 3. 分隔符拆包器 DelimiterBasedFrameDecoder
 * DelimiterBasedFrameDecoder 是行拆包器的通用版本，只不过我们可以自定义分隔符。
 *
 * 4. 基于长度域拆包器 LengthFieldBasedFrameDecoder
 * 最后一种拆包器是最通用的一种拆包器，
 * 只要你的自定义协议中包含长度域字段，
 * 均可以使用这个拆包器来实现应用层拆包。
 */

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import top.paakciu.proxy.core.protocal.codec.PacketCodec;


/**
 * 这个类用于解决粘包问题的自动拆包类，也用于快速解码是不是符合相应的自由协议魔数
 * 由于是判断相应的自由协议，所以一定是深度绑定{@link PacketCodec}这个类的
 *
 * 这个类十分重要，不能共享，使用的时候一定是一个连接 new一个
 *
 * @author paakciu
 * @ClassName: PreFrameDecoder
 * @date: 2021/3/3 20:54
 * @see PacketCodec
 */
public class PreFrameDecoder extends LengthFieldBasedFrameDecoder {

    public PreFrameDecoder() {
        /**
         * 深度绑定{@link PacketCodec}
         */
        super(PacketCodec.MAXFRAMELENGTH, PacketCodec.LENGTHFIELDOFFSET, PacketCodec.LENGTHFIELDLENGTH);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        // 屏蔽非本协议的客户端
        if (in.getInt(in.readerIndex()) != PacketCodec.MAGIC_INT) {
            //System.out.println("PreFrameDecoder识别魔数失败");
            ctx.channel().close();
            return null;
        }
        //解码成功
        //System.out.println("经测试，PreFrameDecoder的解码器成功识别魔数");
        return super.decode(ctx, in);
    }
}
