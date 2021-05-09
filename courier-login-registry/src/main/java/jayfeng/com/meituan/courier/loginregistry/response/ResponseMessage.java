package jayfeng.com.meituan.courier.loginregistry.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 返回信息实体
 * @author JayFeng
 * @date 2020/4/29
 */
@Data
@AllArgsConstructor
public class ResponseMessage {

    /**
     * 请求码 去 redis 取
     */
    private Integer code;

    /**
     * 请求信息
     */
    private String requestMessage;

    /**
     * 响应码，正常请求都是 200
     * 出异常为 5xx
     * 拒绝处理等为 4xx
     * 未知异常为 999
     */
    private Integer responseCode;

    /**
     * 请求结果
     * 成功(0) or 失败(1)
     */
    private Integer requestStatus;

    /**
     * 请求数据
     */
    private Object responseData;

}
