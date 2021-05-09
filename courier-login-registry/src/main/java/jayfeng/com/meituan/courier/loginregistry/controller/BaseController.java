package jayfeng.com.meituan.courier.loginregistry.controller;

import jayfeng.com.meituan.courier.loginregistry.response.RequestFailMessage;
import jayfeng.com.meituan.courier.loginregistry.response.ResponseData;
import jayfeng.com.meituan.courier.loginregistry.response.ResponseMessage;
import org.springframework.stereotype.Controller;

/**
 * 基础控制层
 * @author JayFeng
 * @date 2020/4/1
 */
@Controller
public class BaseController {

    private static final String REQUEST_SUCCESS = "request success";
    private static final String REQUEST_FAIL = "request fail";
    private static final Integer SUCCESS_RESPONSE_CODE = 200;
    private static final Integer REQUEST_SUCCESS_STATUS = 0;
    private static final Integer REQUEST_FAIL_STATUS = 1;

    /**
     * 获取请求码, 去 redis 取
     * @return
     */
    private Integer getRequestCode() {
        return 1;
    }

    /**
     * 请求成功，带返回数据
     * @param responseData 响应数据
     * @return 返回 ResponseMessage 对象
     */
    protected ResponseMessage requestSuccess(ResponseData responseData) {
        return new ResponseMessage(getRequestCode(), REQUEST_SUCCESS, SUCCESS_RESPONSE_CODE, REQUEST_SUCCESS_STATUS, responseData.responseData);
    }

    /**
     * 请求失败, 带请求信息和失败信息
     * @param failCode 失败码
     * @param message 失败信息
     * @param type 失败类型
     * @return 返回响应信息
     */
    protected ResponseMessage requestFail(Integer failCode, String message, String type) {
        RequestFailMessage failMessage = new RequestFailMessage(failCode, message, type);
        return new ResponseMessage(getRequestCode(), REQUEST_FAIL, failMessage.getFailCode(), REQUEST_FAIL_STATUS, failMessage);
    }

}
