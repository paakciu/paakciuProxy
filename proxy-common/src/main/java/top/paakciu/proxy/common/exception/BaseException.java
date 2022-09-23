package top.paakciu.proxy.common.exception;


import top.paakciu.proxy.common.enums.ExceptionEnum;

/**
 * @author paakciu
 * @ClassName: BaseException
 * @since: 2022/5/9 12:12
 */
public class BaseException extends RuntimeException{
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

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseException(String message) {
        super(message);
        this.code = ExceptionEnum.DEFAULT.getCode();
        this.message = message;
    }

    public static BaseException with(String code, String message){
        return new BaseException(code, message);
    }

    public static BaseException with(ExceptionEnum exceptionEnum){
        return new BaseException(exceptionEnum.getCode(), exceptionEnum.getMessage());
    }
}
