package jayfeng.com.meituan.courier.loginregistry.exception;

/**
 * 参数为空异常
 * @author JayFeng
 * @date 2021/4/29
 */
public class ParamEmptyException extends RuntimeException {

    public ParamEmptyException(String message) {
        super(message);
    }

}
