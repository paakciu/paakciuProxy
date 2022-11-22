package top.paakciu.proxy.common.util;

import java.util.UUID;

/**
 * @Classname UuidUtil
 * @Date 2022/11/21 22:40
 * @Created by paakciu
 */
@Deprecated
public class UuidUtil {

    public static String getUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static void main(String[] args) {
        String uuid = getUUID();
        System.out.println(uuid);
    }
}
