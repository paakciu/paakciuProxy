package top.paakciu.proxy.core.protocal.serializer;

/**
 * 防腐层 对序列化算法进行抽象
 * @author paakciu
 * @date 2022/05/05
 */
public interface Serializer {
    /**
     * 序列化算法，获取具体的序列化算法标识
     * @return
     */
    byte getSerializerAlgorithm();

    /**
     * java对象转成二进制
     * @param object
     * @return
     */
    byte[] serialize(Object object);

    /**
     * 二进制转换成java对象
     * @param clazz
     * @param bytes
     * @param <M>
     * @return
     */
    <M> M deserialize(Class<M> clazz,byte[] bytes);


}
