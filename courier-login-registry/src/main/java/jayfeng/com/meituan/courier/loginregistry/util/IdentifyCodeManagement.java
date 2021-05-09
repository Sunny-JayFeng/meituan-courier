package jayfeng.com.meituan.courier.loginregistry.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import jayfeng.com.meituan.courier.loginregistry.redis.RedisService;
import jayfeng.com.meituan.courier.loginregistry.service.impl.AccessKeyService;
import jayfeng.com.meituan.rpc.accesskey.bean.AccessKey;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author JayFeng
 * @date 2021/4/10
 */
@Slf4j
@Component
public class IdentifyCodeManagement {

    private static Random random = new Random();
    private Logger logger = LoggerFactory.getLogger(IdentifyCodeManagement.class);

    private static final Integer IDENTIFY_CODE_ACCESS_KEY = 0;

    @Autowired
    private RedisService redisService;
    @Autowired
    private AccessKeyService accessKeyService;

    /**
     * 获取一个验证码
     * @param phone 手机号
     * @return 返回验证码
     */
    public String getIdentifyCode(String phone) {
        String identifyCode = redisService.getIdentifyCode(phone);
        if (identifyCode == null) {
            ReentrantLock lock = new ReentrantLock();
            lock.lock();
            try {
                identifyCode = createIdentifyCode(phone);
            } finally {
                lock.unlock();
            }
        }
        addIdentifyCodeToRedis(phone, identifyCode);
        log.info("getIdentifyCode, phone: {}, identifyCode: {}", phone, identifyCode);
        return identifyCode;
    }

    /**
     * 创建一个验证码
     * @param phone 手机号
     * @return 返回验证码
     */
    private String createIdentifyCode(String phone) {
        StringBuilder identifyCode = new StringBuilder("");
        for (int i = 1; i <= 6; i ++) {
            identifyCode.append(random.nextInt(8) + 1);
        }
        addIdentifyCodeToRedis(phone, identifyCode.toString());
        log.info("createIdentifyCode, phone: {}, identifyCode: {}", phone, identifyCode);
        return identifyCode.toString();
    }

    /**
     * 将验证码添加到 redis 中
     * @param phone 手机号
     * @param identifyCode 验证码
     */
    private void addIdentifyCodeToRedis(String phone, String identifyCode) {
        redisService.addIdentifyCode(phone, identifyCode);
    }

    /**
     * 移除一个验证码
     * @param phone 手机号
     */
    public void removeIdentifyCode(String phone) {
        Boolean result = redisService.removeIdentifyCode(phone);
        if (result == null || result) {
            log.info("removeIdentifyCode, 验证码移除成功, phone: {}", phone);
        } else {
            log.info("removeIdentifyCode, 验证码移除失败, phone: {}", phone);
        }
    }

    /**
     * 发送短信验证码
     * @param phone 手机号
     * @param identifyCode 验证码
     * @return 返回验证码发送结果
     */
    public String sendIdentifyCode(String phone, String identifyCode) {
        AccessKey accessKey = accessKeyService.getAccessKey(IDENTIFY_CODE_ACCESS_KEY);
        if (accessKey == null) {
            logger.info("sendIdentifyCode 验证码发送失败, 无法取得密钥");
            return null;
        }
        log.info("sendIdentifyCode 发送验证码, 类型: {}, 密钥accessKey: {}", IDENTIFY_CODE_ACCESS_KEY, accessKey);
        phone = phone.replaceAll(" ", "");
        DefaultProfile profile = DefaultProfile.getProfile(accessKey.getRegionId(), accessKey.getAccessKeyId(), accessKey.getSecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "仿美团");
        request.putQueryParameter("TemplateCode", "SMS_201653068");
        request.putQueryParameter("TemplateParam", "{\"identifyCode\": \"" + identifyCode + "\"}");

        try {
            CommonResponse response = client.getCommonResponse(request);
            logger.info("sendIdentifyCode, 验证码发送结果, responseData: {}", response.getData());
            return response.getData();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }

}
