package jayfeng.com.meituan.courier.loginregistry.dao;

import jayfeng.com.meituan.courier.loginregistry.bean.Courier;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * 骑手登录注册持久层
 * @author JayFeng
 * @date 2021/4/29
 */
@Repository
public interface CourierDao {

    /**
     * 新增一个骑手
     * @param courier 骑手
     */
    @Insert("INSERT INTO `courier`(`name`, `birthday`, `phone`, `password`, `email`, `id_card`, " +
            "`received_orders`, `time_out_times`, `praise_times`, `negative_comment_times`, " +
            "`is_valid`, `create_time`, `update_time`) " +
            "VALUES (#{courier.name}, #{courier.birthday}, #{courier.phone}, #{courier.password}," +
            "#{courier.email}, #{courier.idCard}, #{courier.receivedOrders}, #{courier.timeOutTimes}," +
            "#{courier.praiseTimes}, #{courier.negativeCommentTimes}, #{courier.isValid}, " +
            "#{courier.createTime}, #{courier.updateTime})")
    void insertCourier(@Param("courier") Courier courier);

    /**
     * 更新骑手是否有效
     * @param courierId 骑手 id
     * @param isValid 是否有效
     * @param updateTime 更新时间
     */
    @Update("UPDATE `courier` SET `is_valid` = #{isValid}, `update_time` = #{updateTime} WHERE `id` = #{courierId}")
    void updateCourierIsValid(@Param("courierId") Integer courierId, @Param("isValid") Byte isValid, @Param("updateTime") Long updateTime);

    /**
     * 更新密码
     * @param courierId 骑手 id
     * @param password 密码
     * @param updateTime 更新时间
     */
    @Update("UPDATE `courier` SET `password` = #{password}, `update_time` = #{updateTime} WHERE `id` = #{courierId}")
    void updateCourierPassword(@Param("courierId") Integer courierId, @Param("password") String password, @Param("updateTime") Long updateTime);

    /**
     * 更新出生日期
     * @param courierId 骑手 id
     * @param birthday 出生日期
     * @param updateTime 更新时间
     */
    @Update("UPDATE `courier` SET `birthday` = #{birthday}, `update_time` = #{updateTime} WHERE `id` = #{courierId}")
    void updateCourierBirthday(@Param("courierId") Integer courierId, @Param("birthday") String birthday, @Param("updateTime") Long updateTime);

    /**
     * 更新绑定邮箱
     * @param courierId 骑手 id
     * @param email 邮箱
     * @param updateTime 更新时间
     */
    @Update("UPDATE `courier` SET `email` = #{email}, `update_time` = #{updateTime} WHERE `id` = #{courierId}")
    void updateCourierEmail(@Param("courierId") Integer courierId, @Param("email") String email, @Param("updateTime") Long updateTime);

    /**
     * 根据手机号查询骑手
     * @param phone 手机号
     * @return 返回骑手信息
     */
    @Select("SELECT `name`, `birthday`, `phone`, `password`, `email`, `received_orders`, `time_out_times`, `praise_times`, `negative_comment_times` " +
            "FROM courier WHERE `phone` = #{phone}")
    Courier selectOneByPhone(@Param("phone") String phone);

    /**
     * 根据邮箱查询骑手
     * @param email 邮箱
     * @return 返回骑手信息
     */
    @Select("SELECT `name`, `birthday`, `phone`, `email`, `received_orders`, `time_out_times`, `praise_times`, `negative_comment_times` " +
            "FROM courier WHERE `email` = #{email}")
    Courier selectOneByEmail(@Param("email") String email);

    /**
     * 根据身份证查询骑手
     * @param idCard 身份证
     * @return 返回骑手信息
     */
    @Select("SELECT `name`, `birthday`, `phone`, `email`, `received_orders`, `time_out_times`, `praise_times`, `negative_comment_times` " +
            "FROM courier WHERE `id_card` = #{idCard}")
    Courier selectOneByIdCard(@Param("idCard") String idCard);

    /**
     * 根据 id 查询骑手
     * @param courierId 骑手 id
     * @return 返回骑手信息
     */
    @Select("SELECT * FROM `courier` WHERE `id` = #{courierId}")
    Courier selectOneById(@Param("courierId") Integer courierId);

}
