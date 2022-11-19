package top.silwings.admin.common;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @ClassName Payload
 * @Description 载荷
 * @Author Silwings
 * @Date 2022/11/19 12:55
 * @Since
 **/
@Getter
@Setter
public class Payload<T> {

    /**
     * token的唯一标示,JTI
     */
    private String id;

    /**
     * 过期时间
     */
    private Date expiration;

    /**
     * 用户信息
     */
    private T userInfo;
}