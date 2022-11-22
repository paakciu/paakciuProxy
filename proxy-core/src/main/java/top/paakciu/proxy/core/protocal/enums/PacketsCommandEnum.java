package top.paakciu.proxy.core.protocal.enums;



import top.paakciu.proxy.core.protocal.packet.base.HeartBeatPacket;
import top.paakciu.proxy.core.protocal.packet.special.ProxyPacket;

import java.util.HashMap;

/**
 * @author paakciu
 * @EnumName: PacketsCommandEnum
 * @since: 2022/5/9 12:23
 */
public enum PacketsCommandEnum {
    HEARTBEAT(1, HeartBeatPacket.class),
    PROXY(2, ProxyPacket.class),
    ;


    private byte command;

    private Class clazz;

    static HashMap<Byte,Class> hashMap=new HashMap();
    static {
        for (PacketsCommandEnum value : PacketsCommandEnum.values()) {
            hashMap.put(value.getCommand(),value.getClazz());
        }
    }

    PacketsCommandEnum(int command, Class clazz) {
        this.command = (byte)command;
        this.clazz = clazz;
    }

    public static Class getRequestType(byte command){
        return hashMap.getOrDefault(command,null);
    }

    public static byte getTypeByClass(Class clazz){
        PacketsCommandEnum[] values = PacketsCommandEnum.values();
        for (PacketsCommandEnum value : values) {
            if(value.getClazz().equals(clazz)){
                return value.getCommand();
            }
        }
        return 0;
    }
    public byte getCommand() {
        return command;
    }

    public Class getClazz() {
        return clazz;
    }
}
