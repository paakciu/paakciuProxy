package top.paakciu.proxy.common.enums;

/**
 * @author paakciu
 * @EnumName: ExceptionEnum
 * @since: 2022/5/9 12:15
 */
public enum ExceptionEnum {
    COMMONE_ERROR("9999","未知异常"),
    //业务异常0-1xxxx=============================
    DEFAULT("1024","自定义异常"),

    //基础系统异常2xxxxx=============================
    SERVICE_NAME_EMPTY_ERROR("20001","服务名称为空"),
    ;

    /**
     * 错误编码
     * @author paakciu
     * @since 2022/5/9:12:13
     */
    private String code;

    /**
     * 错误信息
     * @author paakciu
     * @since 2022/5/9:12:13
     */
    private String message;

    ExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
