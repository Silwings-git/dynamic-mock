package cn.silwings.admin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import cn.silwings.core.common.EnableStatus;
import cn.silwings.core.common.Identity;

import java.util.Date;
import java.util.List;

/**
 * @ClassName MockHandlerSummaryDto
 * @Description MockHandlerSummaryDto
 * @Author Silwings
 * @Date 2022/11/9 23:22
 * @Since
 **/
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MockHandlerSummaryDto {

    /**
     * 项目id
     */
    private Identity projectId;

    /**
     * 处理器id
     */
    private Identity handlerId;

    /**
     * 启用状态.ture-启用,false-停用
     */
    private EnableStatus enableStatus;

    /**
     * 名称
     */
    private String name;

    /**
     * 支持的请求方式
     */
    private List<HttpMethod> httpMethods;

    /**
     * 支持的请求地址
     */
    private String requestUri;

    /**
     * 自定义标签
     */
    private String label;

    /**
     * 延迟执行时间
     */
    private int delayTime;

    /**
     * 最后更新时间
     */
    private Date updateTime;

}