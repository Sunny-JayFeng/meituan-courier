package jayfeng.com.meituan.courier.loginregistry.service.impl;

import com.google.gson.Gson;
import jayfeng.com.meituan.courier.loginregistry.bean.Courier;
import jayfeng.com.meituan.courier.loginregistry.constant.CookieConstant;
import jayfeng.com.meituan.courier.loginregistry.constant.RedisConstant;
import jayfeng.com.meituan.courier.loginregistry.dao.CourierDao;
import jayfeng.com.meituan.courier.loginregistry.exception.RequestForbiddenException;
import jayfeng.com.meituan.courier.loginregistry.redis.RedisService;
import jayfeng.com.meituan.courier.loginregistry.response.ResponseData;
import jayfeng.com.meituan.courier.loginregistry.service.CourierService;
import jayfeng.com.meituan.courier.loginregistry.util.CookieManagement;
import jayfeng.com.meituan.courier.loginregistry.util.EncryptUtil;
import jayfeng.com.meituan.courier.loginregistry.util.IdentifyCodeManagement;
import jayfeng.com.meituan.courier.loginregistry.util.PatternMatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

/**
 * 骑手登录注册业务层
 * @author JayFeng
 * @date 2021/4/29
 */
@Slf4j
@Service
public class CourierServiceImpl implements CourierService {

    @Autowired
    private CourierDao courierDao;
    @Autowired
    private IdentifyCodeManagement identifyCodeManagement;
    @Autowired
    private CookieManagement cookieManagement;
    @Autowired
    private PatternMatch patternMatch;
    @Autowired
    private EncryptUtil encryptUtil;
    @Autowired
    private RedisService redisService;

    private Gson gson = new Gson();

    /**
     * 通过手机验证码登录
     * @param paramsMap phone - identifyCode
     * @return 返回
     */
    @Override
    public ResponseData loginByCode(Map<String, String> paramsMap, HttpServletResponse response) {
        String phone = paramsMap.get("phone");
        String identifyCode = paramsMap.get("identifyCode");
        if (ObjectUtils.isEmpty(phone) || !patternMatch.isPhone(phone) || ObjectUtils.isEmpty(identifyCode)) {
            throw new RequestForbiddenException("您无权访问该服务");
        }
        Courier courier = courierDao.selectOneByPhone(phone);
        if (courier == null) {
            log.info("loginByCode 登录失败 骑手不存在");
            return ResponseData.createFailResponseData("loginByCodeInfo", false, "骑手未注册", "courier_never_registry");
        }
        if (checkIdentifyCode(phone, identifyCode)) {
            log.info("loginByCode 登录成功 courier: {}", courier);
            courier.setPassword(null);
            setCookie(response, courier);
            identifyCodeManagement.removeIdentifyCode(phone);
            return ResponseData.createSuccessResponseData("loginByCodeInfo", courier);
        } else {
            log.info("loginByCode 登录失败");
            return ResponseData.createFailResponseData("loginByCodeInfo", false, "验证码错误", "identify_code_error");
        }
    }

    /**
     * 检查验证码是否正确
     * @param phone 手机号
     * @param identifyCode 验证码
     * @return
     */
    private boolean checkIdentifyCode(String phone, String identifyCode) {
        String realIdentifyCode = identifyCodeManagement.getIdentifyCode(phone);
        boolean result = identifyCode.equals(realIdentifyCode);
        log.info("checkIdentifyCode 验证码校验结果 result: {}, phone: {}, identifyCode: {}, realIdentifyCode: {}", result, phone, identifyCode, realIdentifyCode);
        return result;
    }

    /**
     * 通过密码登录
     * @param paramsMap phone - password
     * @return 返回
     */
    @Override
    public ResponseData loginByPassword(Map<String, String> paramsMap, HttpServletResponse response) {
        String phone = paramsMap.get("phone");
        String password = paramsMap.get("password");
        if (ObjectUtils.isEmpty(phone) || !patternMatch.isPhone(phone) ||
            ObjectUtils.isEmpty(password) || !patternMatch.checkPassword(password)) {
            throw new RequestForbiddenException("您无权访问该服务");
        }
        Courier courier = courierDao.selectOneByPhone(phone);
        if (courier == null) {
            log.info("loginByPassword 登录失败 骑手不存在");
            return ResponseData.createFailResponseData("loginByPasswordInfo", false, "骑手未注册", "courier_never_registry");
        }
        if (encryptUtil.matches(password, courier.getPassword())) {
            log.info("loginByPassword 登录成功 courier: {}", courier);
            courier.setPassword(null);
            setCookie(response, courier);
            return ResponseData.createSuccessResponseData("loginByPasswordInfo", courier);
        } else {
            log.info("loginByPassword 登录失败, 密码错误");
            return ResponseData.createFailResponseData("loginByPasswordInfo", false, "登录失败, 密码错误", "password_error");
        }
    }

    /**
     * 骑手退出登录
     * @param request 请求
     * @param response 响应
     * @return 返回
     */
    @Override
    public ResponseData courierLogout(HttpServletRequest request, HttpServletResponse response) {
        String uuid = cookieManagement.removeCookie(response, request, CookieConstant.COURIER_KEY.getCookieKey());
        redisService.deleteCourierUUID(RedisConstant.COURIER_UUID_MAP.getRedisMapKey(), uuid);
        log.info("courierLogout 骑手退出登录成功 uuid: {}", uuid);
        return ResponseData.createSuccessResponseData("courierLogoutInfo", true);
    }

    /**
     * 设置 cookie
     * @param response 响应
     * @param courier 骑手
     */
    private void setCookie(HttpServletResponse response, Courier courier) {
        String uuid = UUID.randomUUID().toString();
        log.info("setCookie 设置cookie uuid: {}", uuid);
        cookieManagement.setCookie(response, CookieConstant.COURIER_KEY.getCookieKey(), uuid);
        redisService.addCourierUUID(RedisConstant.COURIER_UUID_MAP.getRedisMapKey(), uuid, gson.toJson(courier));
    }

    /**
     * 检查手机号是否已被注册
     * @param phone 手机号
     * @return 返回
     */
    @Override
    public ResponseData checkPhoneIsRegistered(String phone) {
        if (ObjectUtils.isEmpty(phone) || !patternMatch.isPhone(phone)) throw new RequestForbiddenException("您无权访问该服务");
        Courier courier = courierDao.selectOneByPhone(phone);
        boolean result = !(courier == null);
        log.info("checkPhoneIsRegistered 检查手机号是否已经被注册结果: {}", result);
        log.info("checkPhoneIsRegistered courier: {}", courier);
        return ResponseData.createSuccessResponseData("checkPhoneIsRegisteredInfo", result);
    }

    /**
     * 检查邮箱是否已被注册
     * @param email 邮箱
     * @return 返回
     */
    @Override
    public ResponseData checkEmailIsRegistered(String email) {
        if (ObjectUtils.isEmpty(email) || !patternMatch.isEmail(email)) throw new RequestForbiddenException("您无权访问该服务");
        Courier courier = courierDao.selectOneByEmail(email);
        boolean result = !(courier == null);
        log.info("checkEmailIsRegistered 检查邮箱是否已被注册结果: {}", result);
        log.info("checkEmailIsRegistered courier: {}", courier);
        return ResponseData.createSuccessResponseData("checkEmailIsRegisteredInfo", result);
    }

    /**
     * 检查身份证是否已被注册
     * @param idCard 身份证
     * @return 返回
     */
    @Override
    public ResponseData checkIdCardIsRegistered(String idCard) {
        if (ObjectUtils.isEmpty(idCard) || !patternMatch.isIdCard(idCard)) throw new RequestForbiddenException("您无权访问该服务");
        Courier courier = courierDao.selectOneByIdCard(idCard);
        boolean result = !(courier == null);
        log.info("checkIdCardIsRegistered 检查身份证是否已被注册结果: {}", result);
        log.info("checkIdCardIsRegistered courier: {}", courier);
        return ResponseData.createSuccessResponseData("checkIdCardIsRegisteredInfo", result);
    }

    /**
     * 骑手注册
     * @param identifyCode 验证码
     * @param courier 骑手
     * @return 返回
     */
    @Override
    public ResponseData courierRegistry(String identifyCode, Courier courier) {
        String phone = courier.getPhone();
        String password = courier.getPassword();
        String email = courier.getEmail();
        String idCard = courier.getIdCard();
        if (ObjectUtils.isEmpty(phone) || !patternMatch.isPhone(phone) || courierDao.selectOneByPhone(phone) != null ||
            ObjectUtils.isEmpty(password) || !patternMatch.checkPassword(password) ||
            ObjectUtils.isEmpty(email) || !patternMatch.isEmail(email) || courierDao.selectOneByEmail(email) != null ||
            ObjectUtils.isEmpty(idCard) || !patternMatch.isIdCard(idCard) || courierDao.selectOneByIdCard(idCard) != null) {
            throw new RequestForbiddenException("您无权访问该服务");
        }
        String realIdentifyCode = identifyCodeManagement.getIdentifyCode(phone);
        if (identifyCode.equals(realIdentifyCode)) {
            courier.setPassword(encryptUtil.encrypt(courier.getPassword()));
            courier.setIsValid((byte) 1);
            courier.setCreateTime(System.currentTimeMillis());
            courier.setUpdateTime(courier.getCreateTime());
            courierDao.insertCourier(courier);
            log.info("courierRegistry 注册成功 courier: {}", courier);
            identifyCodeManagement.removeIdentifyCode(phone);
            return ResponseData.createSuccessResponseData("courierRegistryInfo", true);
        } else {
            log.info("courierRegistry 注册失败, 验证码错误 identifyCode: {}, realIdentifyCode: {}", identifyCode, realIdentifyCode);
            return ResponseData.createFailResponseData("courierRegistryInfo", false, "验证码错误", "identify_code_error");
        }

    }

    /**
     * 设置骑手是否有效
     * @param paramsMap 参数
     * phone -- 手机号
     * isValid -- 是否有效
     * @return 返回
     */
    @Override
    public ResponseData setCourierIsValid(Map<String, String> paramsMap) {
        String phone = paramsMap.get("phone");
        String isValid = paramsMap.get("isValid");
        if (ObjectUtils.isEmpty(phone) || patternMatch.isPhone(phone) || ObjectUtils.isEmpty(isValid)) throw new RequestForbiddenException("您无权访问该服务");
        Courier courier = courierDao.selectOneByPhone(phone);
        if (courier == null) {
            log.info("setCourierIsValid 设置骑手是否有效失败, 骑手不存在 phone: {}", phone);
            throw new RequestForbiddenException("您无权访问该服务");
        }
        log.info("setCourierIsValid 设置骑手是否有效 courier: {}, isValid: {}", courier, isValid);
        try {
            courierDao.updateCourierIsValid(courier.getId(), Byte.parseByte(isValid), System.currentTimeMillis());
        } catch (NumberFormatException e) {
            log.info("setCourierIsValid 设置骑手是否有效失败, 状态异常 isValid: {}", isValid);
            throw new RequestForbiddenException("您无权访问该服务");
        }
        return ResponseData.createSuccessResponseData("setCourierIsValidInfo", true);
    }

    /**
     * 骑手修改密码
     * @param paramsMap 参数
     * phone -- 手机号
     * newPassword -- 新密码
     * identifyCode -- 验证码
     * @return 返回
     */
    @Override
    public ResponseData courierChangePassword(Map<String, String> paramsMap) {
        String phone = paramsMap.get("phone");
        String newPassword = paramsMap.get("newPassword");
        String identifyCode = paramsMap.get("identifyCode");
        Courier courier = courierDao.selectOneByPhone(phone);
        if (ObjectUtils.isEmpty(phone) || !patternMatch.isPhone(phone) || courier == null ||
            ObjectUtils.isEmpty(newPassword) || !patternMatch.checkPassword(newPassword) || ObjectUtils.isEmpty(identifyCode)) {
            throw new RequestForbiddenException("您无权访问");
        }
        String realIdentifyCode = identifyCodeManagement.getIdentifyCode(phone);
        if (identifyCode.equals(realIdentifyCode)) {
            log.info("courierChangePassword 骑手修改密码, courier: {}", courier);
            courierDao.updateCourierPassword(courier.getId(), encryptUtil.encrypt(newPassword), System.currentTimeMillis());
            return ResponseData.createSuccessResponseData("courierChangePasswordInfo", true);
        } else {
            log.info("courierChangePassword 骑手修改密码失败, 验证码错误. identifyCode: {}, realIdentifyCode: {}", identifyCode, realIdentifyCode);
            return ResponseData.createFailResponseData("courierChangePasswordInfo", false, "验证码错误", "identify_code_error");
        }
    }

    /**
     * 骑手修改出生日期
     * @param paramsMap 参数
     * phone -- 手机号
     * birthday -- 出生日期
     * @return 返回
     */
    @Override
    public ResponseData courierChangeBirthday(Map<String, String> paramsMap) {
        String phone = paramsMap.get("phone");
        String birthday = paramsMap.get("birthday");
        Courier courier = courierDao.selectOneByPhone(phone);
        if (ObjectUtils.isEmpty(phone) || !patternMatch.isPhone(phone) || courier == null ||
            ObjectUtils.isEmpty(birthday) || !patternMatch.isBirthday(birthday)) {
            throw new RequestForbiddenException("您无权访问该服务");
        }
        courierDao.updateCourierBirthday(courier.getId(), birthday, System.currentTimeMillis());
        log.info("courierChangeBirthday 修改生日成功 old: {}, new: {}", courier.getBirthday(), birthday);
        return ResponseData.createSuccessResponseData("courierChangeBirthdayInfo", true);
    }

    /**
     * 骑手修改绑定邮箱
     * @param paramsMap 参数
     * phone -- 账号
     * email -- 邮箱
     * newEmail -- 新邮箱
     * identifyCode -- 验证码
     * @return 返回
     */
    @Override
    public ResponseData courierChangeEmail(Map<String, String> paramsMap) {
        String phone = paramsMap.get("phone");
        String newEmail = paramsMap.get("newEmail");
        String identifyCode = paramsMap.get("identifyCode");
        Courier courier = courierDao.selectOneByPhone(phone);
        if (ObjectUtils.isEmpty(phone) || !patternMatch.isPhone(phone) || courier == null ||
            ObjectUtils.isEmpty(newEmail) || !patternMatch.isEmail(newEmail) ||
            ObjectUtils.isEmpty(identifyCode)) {
            throw new RequestForbiddenException("您无权访问该服务");
        }
        String realIdentifyCode = identifyCodeManagement.getIdentifyCode(phone);
        if (identifyCode.equals(realIdentifyCode)) {
            log.info("courierChangeEmail 修改骑手绑定邮箱, courier: {}", courier);
            courierDao.updateCourierEmail(courier.getId(), newEmail, System.currentTimeMillis());
            log.info("courierChangeEmail 修改骑手绑定邮箱成功, oldEmail: {}, newEmail: {}", courier.getEmail(), newEmail);
            return ResponseData.createSuccessResponseData("courierChangeEmailInfo", true);
        } else {
            log.info("courierChangeEmail 修改骑手绑定邮箱失败, 验证码错误 identifyCode: {}, realIdentifyCode: {}", identifyCode, realIdentifyCode);
            return ResponseData.createFailResponseData("courierChangeEmailInfo", false, "验证码错误", "identify_code_error");
        }
    }

    /**
     * 根据手机号查询骑手信息
     * @param phone 手机号
     * @return 返回骑手信息
     */
    @Override
    public ResponseData findCourierByPhone(String phone) {
        if (ObjectUtils.isEmpty(phone) || !patternMatch.isPhone(phone)) throw new RequestForbiddenException("您无权访问该服务");
        Courier courier = courierDao.selectOneByPhone(phone);
        log.info("findCourierByPhone 通过手机号查找骑手信息 courier: {}", courier);
        courier.setPassword(null);
        return ResponseData.createSuccessResponseData("findCourierByPhoneInfo", courier);
    }

    /**
     * 获取短信验证码
     * @param phone 手机号
     * @return 返回
     */
    @Override
    public ResponseData getIdentifyCode(String phone) {
        if (ObjectUtils.isEmpty(phone) || !patternMatch.isPhone(phone)) throw new RequestForbiddenException("您无权访问该服务");
        String identifyCode = identifyCodeManagement.getIdentifyCode(phone);
        identifyCodeManagement.sendIdentifyCode(phone, identifyCode);
        log.info("getIdentifyCode 获取短信验证码 phone: {}, identifyCode: {}", phone, identifyCode);
        return ResponseData.createSuccessResponseData("getIdentifyCodeInfo", identifyCode);
    }

}
