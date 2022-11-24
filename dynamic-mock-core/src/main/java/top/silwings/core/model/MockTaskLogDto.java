package top.silwings.core.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import top.silwings.core.common.Identity;

import java.util.Date;

/**
 * @ClassName MockTaskLogPo
 * @Description 任务日志
 * @Author Silwings
 * @Date 2022/11/23 23:11
 * @Since
 **/
@Getter
@Setter
@Builder
public class MockTaskLogDto {

    /**
     * 日志id
     */
    private Identity logId;

    /**
     * 任务编码
     */
    private String taskCode;

    /**
     * Mock Handler id
     */
    private Identity handlerId;

    /**
     * 任务名
     */
    private String name;

    /**
     * 注册时间
     */
    private Date registrationTime;

    /**
     * 请求信息
     */
    private String requestInfo;

    /**
     * 响应信息
     */
    private String responseInfo;

    /**
     * 请求发起时间
     */
    private Date requestTime;

    /**
     * 耗时
     */
    private Long timing;

}