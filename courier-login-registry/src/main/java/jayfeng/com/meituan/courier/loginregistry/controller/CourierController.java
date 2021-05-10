package jayfeng.com.meituan.courier.loginregistry.controller;

import jayfeng.com.meituan.courier.loginregistry.bean.Courier;
import jayfeng.com.meituan.courier.loginregistry.response.ResponseMessage;
import jayfeng.com.meituan.courier.loginregistry.service.CourierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 骑手登录注册控制层
 * @author JayFeng
 * @date 2021/4/29
 */
@Slf4j
@RestController
@RequestMapping("/meituan/courier/login_registry")
public class CourierController extends BaseController {

    @Autowired
    private CourierService courierService;

    /**
     * 通过手机验证码登录
     * @param paramsMap phone - identifyCode
     * @return 返回
     */
    @PostMapping("/loginByCode")
    public ResponseMessage loginByCode(@RequestBody Map<String, String> paramsMap, HttpServletResponse response) {
        log.info("loginByCode 通过手机验证码登录 paramsMap: {}", paramsMap);
        return requestSuccess(courierService.loginByCode(paramsMap, response));
    }

    /**
     * 通过密码登录
     * @param paramsMap phone - password
     * @return 返回
     */
    @PostMapping("/loginByPassword")
    public ResponseMessage loginByPassword(@RequestBody Map<String, String> paramsMap, HttpServletResponse response) {
        log.info("loginByPassword 通过密码登录 paramsMap: {}", paramsMap);
        return requestSuccess(courierService.loginByPassword(paramsMap, response));
    }

    /**
     * 骑手退出登录
     * @param request 请求
     * @param response 响应
     * @return 返回
     */
    @PutMapping("/courierLogout")
    public ResponseMessage courierLogout(HttpServletRequest request, HttpServletResponse response) {
        log.info("logout 骑手退出登录");
        return requestSuccess(courierService.courierLogout(request, response));
    }

    /**
     * 获取短信验证码
     * @param phone 手机号
     * @return 返回验证码
     */
    @GetMapping("/getIdentifyCode/{phone}")
    public ResponseMessage getIdentifyCode(@PathVariable("phone") String phone) {
        log.info("getIdentifyCode 获取短信验证码 phone: {}", phone);
        return requestSuccess(courierService.getIdentifyCode(phone));
    }

    /**
     * 检查手机号是否已被注册
     * @param phone 手机号
     * @return 返回
     */
    @GetMapping("/checkPhoneIsRegistered/{phone}")
    public ResponseMessage checkPhoneIsRegistered(@PathVariable("phone") String phone) {
        log.info("checkPhoneIsRegistered 检查手机号是否已被注册 phone: {}", phone);
        return requestSuccess(courierService.checkPhoneIsRegistered(phone));
    }

    /**
     * 检查邮箱是否已被注册
     * @param email 邮箱
     * @return 返回
     */
    @GetMapping("/checkEmailIsRegistered")
    public ResponseMessage checkEmailIsRegistered(@RequestParam("email") String email) {
        log.info("checkEmailIsRegistered 检查邮箱是否已被注册 email: {}", email);
        return requestSuccess(courierService.checkEmailIsRegistered(email));
    }

    /**
     * 检查身份证是否已被注册
     * @param idCard 身份证
     * @return 返回
     */
    @GetMapping("/checkIdCardIsRegistered")
    public ResponseMessage checkIdCardIsRegistered(@RequestParam("idCard") String idCard) {
        log.info("checkIdCardIsRegistered 检查身份证是否已被注册 idCard: {}", idCard);
        return requestSuccess(courierService.checkIdCardIsRegistered(idCard));
    }

    /**
     * 骑手注册
     * @param identifyCode 验证码
     * @param courier 骑手
     * @return 返回
     */
    @PostMapping("/courierRegistry/{identifyCode}")
    public ResponseMessage courierRegistry(@PathVariable("identifyCode") String identifyCode, @RequestBody Courier courier) {
        log.info("courierRegistry 骑手注册 identifyCode: {}, courier: {}", identifyCode, courier);
        return requestSuccess(courierService.courierRegistry(identifyCode, courier));
    }

    /**
     * 骑手修改密码
     * @param paramsMap 参数
     * phone -- 手机号
     * newPassword -- 新密码
     * identifyCode -- 验证码
     * @return 返回
     */
    @PutMapping("/courierChangePassword")
    public ResponseMessage courierChangePassword(@RequestBody Map<String, String> paramsMap) {
        log.info("courierChangePassword 骑手修改密码 paramsMap: {}", paramsMap);
        return requestSuccess(courierService.courierChangePassword(paramsMap));
    }

    /**
     * 骑手修改出生日期
     * @param paramsMap 参数
     * phone -- 手机号
     * birthday -- 出生日期
     * @return 返回
     */
    @PutMapping("/courierChangeBirthday")
    public ResponseMessage courierChangeBirthday(@RequestBody Map<String, String> paramsMap) {
        log.info("courierChangeBirthday 骑手修改出生日期 paramsMap: {}", paramsMap);
        return requestSuccess(courierService.courierChangeBirthday(paramsMap));
    }

    /**
     * 骑手修改绑定邮箱
     * @param paramsMap 参数
     * email -- 邮箱
     * newEmail -- 新邮箱
     * identifyCode -- 验证码
     * @return 返回
     */
    @PutMapping("/courierChangeEmail")
    public ResponseMessage courierChangeEmail(@RequestBody Map<String, String> paramsMap) {
        log.info("courierChangeEmail 骑手修改绑定邮箱 paramsMap: {}", paramsMap);
        return requestSuccess(courierService.courierChangeEmail(paramsMap));
    }

    /**
     * 根据手机号查询骑手信息
     * @param phone 手机号
     * @return 返回骑手信息
     */
    @GetMapping("/findCourierByPhone/{phone}")
    public ResponseMessage findCourierByPhone(@PathVariable("phone") String phone) {
        log.info("findCourierByPhone 通过手机号查找骑手信息 phone: {}", phone);
        return requestSuccess(courierService.findCourierByPhone(phone));
    }

    /**
     * 设置骑手是否有效
     * @param paramsMap 参数
     * phone -- 手机号
     * isValid -- 是否有效
     * @return 返回
     */
    @PutMapping("/setCourierIsValid")
    public ResponseMessage setCourierIsValid(@RequestBody Map<String, String> paramsMap) {
        log.info("setCourierIsValid 修改骑手是否有效状态 paramsMap: {}", paramsMap);
        return requestSuccess(courierService.setCourierIsValid(paramsMap));
    }

}
