package jayfeng.com.meituan.courier.loginregistry.util;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

/**
 * 格式校验：手机号、邮箱、密码等等
 * @author JayFeng
 * @date 2020/4/10
 */
@Component
public class PatternMatch {

    /**
     * 校验手机号格式是否正确
     * @param phone
     * @return 返回是否正确
     */
    public Boolean isPhone(String phone) {
        return phone.matches("^1[0-9]{10}$");
    }

    /**
     * 邮箱格式是否正确
     * @param email 邮箱
     * @return 返回是否正确
     */
    public Boolean isEmail(String email) {
        return email.matches("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");
    }

    /**
     * 身份证格式是否正确
     * @param idCard 身份证
     * @return 返回
     */
    public Boolean isIdCard(String idCard) {
        return true;
    }

    /**
     * 出生日期是否正确
     * @param birthday
     * @return
     */
    public Boolean isBirthday(String birthday) {
        if (ObjectUtils.isEmpty(birthday)) return false;
        String[] timeArray = birthday.split("-");
        if (timeArray.length != 3) return false;
        try {
            int year = Integer.parseInt(timeArray[0]);
            int month = Integer.parseInt(timeArray[1]);
            int day = Integer.parseInt(timeArray[2]);
            if (LocalDateTime.now().getYear() - year < 18 || LocalDateTime.now().getYear() - year > 70 ||
                month < 1 || month > 12 || day < 1 || day > 31) return false;
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 密码格式是否符合要求
     * @param password 密码
     * @return 返回是否正确
     */
    public Boolean checkPassword(String password) {
        return password.length() >= 8 && password.length() <=32;
    }

}
