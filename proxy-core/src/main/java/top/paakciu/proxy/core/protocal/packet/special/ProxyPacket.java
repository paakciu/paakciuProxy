package top.paakciu.proxy.core.protocal.packet.special;

import lombok.Data;
import top.paakciu.proxy.core.protocal.packet.base.BasePacket;

/**
 * @Classname ProxyPacket
 * @Date 2022/11/9 22:14
 * @Created by paakciu
 */
@Data
public class ProxyPacket extends BasePacket {
    /**
     * 消息类型
     */
    private byte type;

    /**
     * 消息传输数据
     */
    private byte[] data;
    /**
     * 标记服务侧连接的唯一标识
     */
    private String uuid;

    public static class ProxyPacketType{
        public static final byte CONNECT=1;
        public static final byte DISCONNECT=2;
        public static final byte TRANSFER=3;

    }
}
