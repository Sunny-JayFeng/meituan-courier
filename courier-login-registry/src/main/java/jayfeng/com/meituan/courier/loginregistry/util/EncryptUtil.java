package jayfeng.com.meituan.courier.loginregistry.util;

import jayfeng.com.meituan.courier.loginregistry.exception.ParamEmptyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

/**
 * 加密工具类
 * @author JayFeng
 * @date 2021/04/10
 */
@Component
@Slf4j
public class EncryptUtil {

    @Autowired
    private RandomUtil randomUtil;

    private static final int SALT_LENGTH = 22;

    private String getSalt() {
        return randomUtil.getRandomString(SALT_LENGTH);
    }

    /**
     * 密码加密
     * @param password 明文密码
     * @return 返回加密后的密码
     */
    public String encrypt(String password) {
        String salt = getSalt();
        if (ObjectUtils.isEmpty(password)) {
            throw new ParamEmptyException("必要参数 password 为空");
        }
        if (ObjectUtils.isEmpty(salt)) {
            throw new ParamEmptyException("必要参数 salt 为空");
        }
        return encrypt(password, salt);
    }

    /**
     * 校验密码
     * @param password 明文密码
     * @param encryptedPassword 密文密码
     * @return 返回校验结果
     */
    public boolean matches(String password, String encryptedPassword) {
        if (ObjectUtils.isEmpty(password)) {
            throw new ParamEmptyException("必要参数 password 为空");
        }
        if (ObjectUtils.isEmpty(encryptedPassword)) {
            throw new ParamEmptyException("必要参数 encryptedPassword 为空");
        }
        String salt = getSalt(encryptedPassword);
        String encryptResult = encrypt(password, salt);
        return encryptResult.equals(encryptedPassword);
    }

    /**
     * 加密密码
     * @param password 明文密码
     * @param salt 盐
     * @return 返回加密后的密文
     */
    private String encrypt(String password, String salt) {
        String withoutSaltResult = encryptWithoutSalt(password);
        String withSaltResult = encryptWithSalt(password, salt).toUpperCase();

        StringBuilder result = new StringBuilder(salt);
        for (int i = 0; i < 32; i ++) {
            if (i % 2 == 0) result.append(withoutSaltResult.charAt(i));
            else result.append(withSaltResult.charAt(i));
        }
        return result.toString();
    }

    /**
     * 纯 MD5 加密
     * @param password 明文密码
     * @return 返回加密后的密文
     */
    private String encryptWithoutSalt(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }

    /**
     * 加盐加密
     * @param password 明文密码
     * @param salt 盐
     * @return 返回加密后的密文
     */
    private String encryptWithSalt(String password, String salt) {
        password = password + salt;
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }

    /**
     * 解析加密后的密文得到加密时的盐
     * @param encryptedPassword 加密后的密文
     * @return 返回该密码加密时的盐
     */
    private String getSalt(String encryptedPassword) {
        return encryptedPassword.substring(0, SALT_LENGTH);
    }

    public static void main(String[] args) {
        EncryptUtil encryptUtil = new EncryptUtil();
        System.out.println(encryptUtil.encrypt("123456"));
    }

}
