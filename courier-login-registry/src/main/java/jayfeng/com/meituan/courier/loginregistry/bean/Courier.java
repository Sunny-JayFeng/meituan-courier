package jayfeng.com.meituan.courier.loginregistry.bean;

import lombok.Data;

/**
 * 骑手实体类
 * @author JayFeng
 * @date 2021/4/29
 */
@Data
public class Courier {

    /**
     * 骑手 id
     */
    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 出生年月日
     */
    private String birthday;

    /**
     * 手机号(账号)
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 已接单数
     */
    private Integer receivedOrders;

    /**
     * 超时次数
     */
    private Integer timeoutTimes;

    /**
     * 好评次数
     */
    private Integer praiseTimes;

    /**
     * 差评次数
     */
    private Integer negativeCommentTimes;

    /**
     * 是否有效
     * 0 -- 无效
     * 1 -- 有效
     */
    private Byte isValid;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;

}
