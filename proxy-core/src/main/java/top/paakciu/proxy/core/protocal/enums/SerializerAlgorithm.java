package top.paakciu.proxy.core.protocal.enums;

import top.paakciu.proxy.core.protocal.serializer.JSONSerializer;
import top.paakciu.proxy.core.protocal.serializer.Serializer;

/**
 * 指示用什么序列化方法
 * SerializerAlgorithm
 * @author paakciu
 * @since 2022/05/05 10:04
 */
public enum SerializerAlgorithm {
    JSON(1,"fastJSON", JSONSerializer.INSTANCE),

    ;
    /**
     * 默认的序列化器
     */
    public static SerializerAlgorithm DEFAULT=JSON;

    /**
     * 编码
     */
    byte code;
    /**
     * 描述
     */
    String desc;

    /**
     * 序列化器
     */
    Serializer serializer;



    SerializerAlgorithm(int code, String desc, Serializer serializer) {
        this.code = (byte) code;
        this.desc = desc;
        this.serializer = serializer;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public static Serializer getSerializer(byte serializeAlgorithm) {
        for (SerializerAlgorithm value : SerializerAlgorithm.values()) {
            if(value.equals(serializeAlgorithm)){
                return value.getSerializer();
            }
        }
        return new JSONSerializer();
    }

}