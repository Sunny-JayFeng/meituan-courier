package jayfeng.com.meituan.courier.loginregistry.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 负责产生随机值
 * 用于密码加密的随机盐
 * 用户注册时的随机昵称
 * @author JayFeng
 * @date 2021/4/10
 */
@Component
@Slf4j
public class RandomUtil {

    private static final int DEFAULT_LENGTH = 16;

    /**
     * 不指定随机的字符串长度，默认 16
     * @return 返回随机生成的字符串
     */
    public String getRandomString() {
        return getRandomString(DEFAULT_LENGTH);
    }

    /**
     * 产生随机字符串
     * @param length 要产生的字符串的长度
     * @return 返回随机产生的字符串
     */
    public String getRandomString(int length) {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        while (str.length() < length) {
            int num = random.nextInt(75) + 48;
            if (canUse(num)) str.append((char)num);
        }
        return str.toString();
    }

    /**
     * 随机产生一个字符：大小写字母或下划线
     * @return 返回随机产生的字符
     */
    public Character getRandomLetter() {
        Random random = new Random();
        while (true) {
            int num = random.nextInt(58) + 65;
            if (charIsLetterOrBottomLine(num)) return (char) num;
        }
    }

    /**
     * 字符是否为字母或下划线
     * @param num 字母编码
     * @return 返回是否为字母或下或线
     */
    private boolean charIsLetterOrBottomLine(int num) {
        // 95 是下划线
        return (num >= 65 && num <= 90) || (num >= 97 && num <= 122) || num == 95;
    }

    /**
     * 字符是否为数字
     * @param num 字符编码
     * @return 返回是否为数字
     */
    private boolean charIsNumber(int num) {
        return (num >= 48 && num <= 57);
    }

    /**
     * 随机产生的字符是否为数字，或者大小写字母，或者下划线
     * @param num 字符编码
     * @return 返回字符是否为...
     */
    private boolean canUse(int num) {
        return charIsNumber(num) || charIsLetterOrBottomLine(num);
    }

    public static void main(String[] args) {

    }

}
