package jayfeng.com.meituan.courier.loginregistry.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JayFeng
 * @date 2020/4/29
 */
public class ResponseData {

    public static void main(String[] args) {
        LocalDate date = LocalDate.now();
        LocalDateTime time = LocalDateTime.now();
        System.out.println(time);
        System.out.println(date);
    }

    public Map<Object, Object> responseData = new HashMap<>(4);

    /**
     * 设置请求成功返回信息
     * @param infoKey 请求数据 key
     * @param infoData 请求数据
     */
    private void setSuccessData(String infoKey, Object infoData) {
        responseData.put("success", true);
        responseData.put(infoKey, infoData);
    }

    /**
     * 设置请求失败返回信息
     * @param infoKey 请求数据 key
     * @param infoData 请求数据
     * @param failMessage 失败信息
     */
    private void setFailData(String infoKey, Object infoData, RequestFailMessage failMessage) {
        responseData.put("success", false);
        responseData.put(infoKey, infoData);
        responseData.put("failMessage", failMessage);
    }

    /**
     * 创建请求成功返回数据对象
     * @param infoKey 请求数据 key
     * @param infoData 请求数据
     * @return 返回 ResponseData 对象
     */
    public static ResponseData createSuccessResponseData(String infoKey, Object infoData) {
        ResponseData responseData = new ResponseData();
        responseData.setSuccessData(infoKey, infoData);
        return responseData;
    }

    /**
     * 创建请求失败返回数据对象
     * @param infoKey 请求数据 key
     * @param infoData 请求数据
     * @param failMessage 失败信息
     * @return 返回 ResponseData 对象
     */
    public static ResponseData createFailResponseData(String infoKey, Object infoData, RequestFailMessage failMessage) {
        ResponseData responseData = new ResponseData();
        responseData.setFailData(infoKey, infoData, failMessage);
        return responseData;
    }

    /**
     * 创建请求失败返回数据对象
     * @param infoKey 请求数据 key
     * @param infoData 请求数据
     * @param failMessage 失败信息
     * @param failType 失败类型
     * @return 返回 ResponseData 对象
     */
    public static ResponseData createFailResponseData(String infoKey, Object infoData, String failMessage, String failType) {
        RequestFailMessage failMessageObj = new RequestFailMessage(failMessage, failType);
        return createFailResponseData(infoKey, infoData, failMessageObj);
    }
}
