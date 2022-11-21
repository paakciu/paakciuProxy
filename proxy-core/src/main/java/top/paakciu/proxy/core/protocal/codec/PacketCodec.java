package top.paakciu.proxy.core.protocal.codec;

import io.netty.buffer.ByteBuf;
import top.paakciu.proxy.core.protocal.enums.PacketsCommandEnum;
import top.paakciu.proxy.core.protocal.enums.SerializerAlgorithm;
import top.paakciu.proxy.core.protocal.packet.base.BasePacket;
import top.paakciu.proxy.core.protocal.serializer.Serializer;

/**
 * PacketCodeC
 * 默认的包编码译码器
 * @author paakciu
 * @date 2020/12/21 11:12
 */
public class PacketCodec {

    /**
     * 魔数
     */
    public static final byte[] MAGIC_NUMBER= new byte[]{'P','a','a','k'};
    public static final int MAGIC_INT=1348559211;
    /**
     * 这些是跟包编码/解码相关的参数，一定是绑定这个 类的
     * {@link PacketCodec}
     */

    /**
     * 帧偏移
     */
    public static final int LENGTHFIELDOFFSET=7;
    /**
     * 帧长标识占多少位  结合上面的帧偏移 则表示 bytes=xxxxxxx[yyyy]其中yyyy的帧长值
     */
    public static final int LENGTHFIELDLENGTH=4;
    /**
     * 最大帧长
     */
    public static final int MAXFRAMELENGTH=Integer.MAX_VALUE;

    /**
     * 编码
     * @param byteBuf
     * @param packet
     * @return
     */
    public static ByteBuf encode(ByteBuf byteBuf, BasePacket packet) {
        //序列器序列化成字节流
        byte[] bytes=packet.toBytes();
        //装填编码包
        //  魔数
        byteBuf.writeBytes(MAGIC_NUMBER);
        //  封包协议版本
        byteBuf.writeByte(packet.getVersion());
        //  序列算法标识号
        byteBuf.writeByte(packet.getSerializerAlgorithm());
        //  指令（控制号）
        byteBuf.writeByte(packet.getCommand());
        //  数据长度,这里使用int型，如果带符号长度可携带2G数据，无符号则4G
        byteBuf.writeInt(bytes.length);
        //  数据体
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }

    /**
     * 译码
     * @param byteBuf
     * @return
     */
    public static BasePacket decode(ByteBuf byteBuf) {
        // 读取magic number
        int Magic=byteBuf.readInt();
        if(MAGIC_INT!=Magic){
            return null;
        }
        // 版本号
        byte version = byteBuf.readByte();//skipBytes
        // 序列化算法标识
        byte serializeAlgorithm = byteBuf.readByte();
        // 指令
        byte command = byteBuf.readByte();
        // 数据包长度
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        return toPacket(version,serializeAlgorithm,command, bytes);
    }

    private static BasePacket toPacket(byte version, byte serializeAlgorithm, byte command, byte[] bytes){
        /**
         * 使用{@link PacketsCommandMapping}获取类型
         */
        Class<? extends BasePacket> requestType = PacketsCommandEnum.getRequestType(command);
        /**
         * 使用{@link SerializerAlgorithm}获取序列化方法
         */
        Serializer serializer = SerializerAlgorithm.getSerializer(serializeAlgorithm);

        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }
        //如果出了问题
        return null;
    }
}
