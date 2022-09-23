package top.paakciu.proxy.core.protocal.packet;

import lombok.Data;

/**
 * @author paakciu
 * @ClassName: Rp
 * @since: 2022/5/9 12:32
 */
@Data
public class RpcPacket extends BasePacket{

    private String targetMethod;

    private String targetServiceName;

    private Object[] args;

    private String uuid;

    private Object response;
}
