package jayfeng.com.meituan.courier.loginregistry.util;

import jayfeng.com.meituan.courier.loginregistry.constant.CookieConstant;
import jayfeng.com.meituan.courier.loginregistry.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie 管理
 * @author JayFeng
 * @date 2020/04/10
 */
@Component
@Slf4j
public class CookieManagement {

    @Autowired
    private RedisService redisService;

    /**
     * 创建一个 cookie
     * @param key cookie 的 key
     * @param value cookie 的值
     * @return
     */
    public Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(CookieConstant.MAX_AGE.getMaxAge()); // cookie 在 1 个小时后失效
        cookie.setPath(CookieConstant.PATH.getCookiePath());
        cookie.setDomain(CookieConstant.DO_MAIN.getCookieDoMain());
        return cookie;
    }

    /**
     * 获取当前已登录的骑手的 cookie
     * @param request request
     * @param cookieKey cookie key
     * @param redisMapKey redisMap key
     * @return 返回 cookie
     */
    public Object getLoginSeller(HttpServletRequest request, String cookieKey, String redisMapKey) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                String key = cookie.getName();
                if (cookieKey.equals(key)) {
                    return redisService.getCourierJSON(redisMapKey, cookie.getValue());
                }
            }
        }
        return null;
    }

    /**
     * 删除一个 cookie
     * @param request 获取所有 cookie
     * @param key cookie 的 key
     * @return
     */
    public String removeCookie(HttpServletResponse response, HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (key.equals(cookie.getName())) {
                    log.info("removeCookie, key: {}", key);
                    cookie.setMaxAge(0);
                    // 必须设置相同的 path 和 domain， setMaxAge 才有效
                    cookie.setPath(CookieConstant.PATH.getCookiePath());
                    cookie.setDomain(CookieConstant.DO_MAIN.getCookieDoMain());
                    response.addCookie(cookie);
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 给请求添加一个 cookie
     * @param response 响应，设置 cookie
     * @param key cookie 的 key
     * @param value cookie 的值
     */
    public void setCookie(HttpServletResponse response, String key, String value) {
        Cookie cookie = createCookie(key, value);
        setCookie(response, cookie);
    }

    /**
     * 给请求添加一个 cookie
     * @param response 响应，设置 cookie
     * @param cookie cookie
     */
    public void setCookie(HttpServletResponse response, Cookie cookie) {
        log.info("setCookie, key: {}, value: {}", cookie.getName(), cookie.getValue());
        response.addCookie(cookie);
    }

}
