package top.silwings.core.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @ClassName MockHandlerHttpTaskDefinition
 * @Description Http任务定义
 * @Author Silwings
 * @Date 2022/11/9 23:24
 * @Since
 **/
@Getter
@Builder
public class TaskInfoDto {

    /**
     * 任务名称
     */
    private String name;

    /**
     * 支持条件
     */
    private List<String> support;

    /**
     * 是否异步执行
     */
    private boolean async;

    /**
     * cron
     */
    private String cron;

    /**
     * 执行次数
     */
    private int numberOfExecute;

    /**
     * 请求信息
     */
    private TaskRequestDto request;

}