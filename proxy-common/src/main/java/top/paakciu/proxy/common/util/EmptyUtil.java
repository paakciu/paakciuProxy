package top.paakciu.proxy.common.util;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * 判空工具类
 * 1.字符串                 支持
 * 2.List Coolection       支持
 * 3.Map                   支持
 * 4.Array                 不支持
 * @Classname EmptyUtil
 * @Date 2022/7/28 16:10
 * @Created by paakciu
 */
public class EmptyUtil {

    /**
     * 是否存在空
     * @date: 2022/7/28 16:26
     * @author paakciu
     * @param objects
     * @return
     */
    public static boolean isAnyEmpty(Object... objects){
        if (isArrayEmpty(objects)){
            return true;
        }
        for (Object object : objects) {
            if(isEmpty(object)){
                return true;
            }
        }
        return false;
    }

    /**
     * 是否都为空
     * @date: 2022/7/28 16:26
     * @author paakciu
     * @param objects
     * @return
     */
    public static boolean isAllEmpty(Object... objects){
        if (isArrayEmpty(objects)){
            return true;
        }
        for (Object object : objects) {
            if(!isEmpty(object)){
                return false;
            }
        }
        return true;
    }

    /**
     * 是否为空
     * @date: 2022/7/28 16:26
     * @author paakciu
     * @param object
     * @return
     */
    public static boolean isEmpty(Object object){
        if(Objects.isNull(object)){
            return true;
        } else if(object instanceof CharSequence){
            return isBlank((CharSequence)object);
        } else if(object instanceof Collection){
            return isCollectionEmpty((Collection)object);
        } else if(object instanceof Map){
            return isMapEmpty((Map)object);
        }
        return false;
    }

    /**
     * 是否不为空
     * @date: 2022/7/28 16:26
     * @author paakciu
     * @param object
     * @return
     */
    public static boolean isNotEmpty(Object object){
        return !isEmpty(object);
    }
    private static boolean isBlank(final CharSequence charSequence){
        int strLen;
        if(charSequence == null || (strLen = charSequence.length()) == 0){
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if(!Character.isWhitespace(charSequence.charAt(i))){
                return false;
            }
        }
        return true;
    }
    private static boolean isArrayEmpty(final Object[] array){
        return array == null || array.length == 0;
    }
    private static boolean isMapEmpty(final Map<?,?> map){
        return map == null || map.isEmpty();
    }
    private static boolean isCollectionEmpty(Collection<?> collection){
        return collection == null || collection.isEmpty();
    }
    private static boolean isCollectionEmpty(Map<?,?> map){
        return map == null || map.isEmpty();
    }
}
