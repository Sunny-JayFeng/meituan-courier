package jayfeng.com.meituan.courier.loginregistry.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 请求失败信息
 * @author JayFeng
 * @date 2021/4/29
 */
@Data
public class RequestFailMessage {

    /**
     * 请求时间
     */
    private LocalDateTime requestTime;

    /**
     * 请求失败码
     */
    private Integer failCode;

    /**
     * 失败信息
     */
    private String message;

    /**
     * 失败类型
     */
    private String type;

    /**
     * 返回数据
     */
    private Object data;

    /**
     * 创建请求失败信息短袖
     * @param failCode 失败码
     * @param message 失败信息
     * @param type 失败类型
     */
    public RequestFailMessage(Integer failCode, String message, String type) {
        this.requestTime = LocalDateTime.now();
        this.failCode = failCode;
        this.message = message;
        this.type = type;
    }

    /**
     * 创建请求失败信息对象
     * @param message 失败信息
     * @param type 失败类型
     */
    public RequestFailMessage(String message, String type) {
        this.requestTime = LocalDateTime.now();
        this.failCode = 200;
        this.message = message;
        this.type = type;
    }

}
