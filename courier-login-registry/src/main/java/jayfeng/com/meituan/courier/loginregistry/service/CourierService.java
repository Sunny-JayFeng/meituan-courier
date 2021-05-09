package jayfeng.com.meituan.courier.loginregistry.service;

import jayfeng.com.meituan.courier.loginregistry.bean.Courier;
import jayfeng.com.meituan.courier.loginregistry.response.ResponseData;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 骑手登录注册业务层
 * @author JayFeng
 * @date 2021/4/29
 */
@Service
public interface CourierService {

    /**
     * 骑手注册
     * @param courier 骑手
     * @return 返回
     */
    ResponseData courierRegistry(Courier courier);

    /**
     * 设置骑手是否有效
     * @param paramsMap 参数
     * phone -- 手机号
     * isValid -- 是否有效
     * @return 返回
     */
    ResponseData setCourierIsValid(Map<String, String> paramsMap);

    /**
     * 骑手修改密码
     * @param paramsMap 参数
     * phone -- 手机号
     * newPassword -- 新密码
     * identifyCode -- 验证码
     * @return 返回
     */
    ResponseData courierChangePassword(Map<String, String> paramsMap);

    /**
     * 骑手修改出生日期
     * @param paramsMap 参数
     * phone -- 手机号
     * birthday -- 出生日期
     * @return 返回
     */
    ResponseData courierChangeBirthday(Map<String, String> paramsMap);

    /**
     * 骑手修改绑定邮箱
     * @param paramsMap 参数
     * email -- 邮箱
     * newEmail -- 新邮箱
     * identifyCode -- 验证码
     * @return 返回
     */
    ResponseData courierChangeEmail(Map<String, String> paramsMap);

    /**
     * 根据手机号查询骑手信息
     * @param phone 手机号
     * @return 返回骑手信息
     */
    ResponseData findCourierByPhone(String phone);

    /**
     * 检查手机号是否已被注册
     * @param phone 手机号
     * @return 返回
     */
    ResponseData checkPhoneIsRegistered(String phone);

    /**
     * 检查邮箱是否已被注册
     * @param email 邮箱
     * @return 返回
     */
    ResponseData checkEmailIsRegistered(String email);

    /**
     * 检查身份证是否已被注册
     * @param idCard 身份证
     * @return 返回
     */
    ResponseData checkIdCardIsRegistered(String idCard);

}
