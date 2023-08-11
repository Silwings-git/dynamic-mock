package top.silwings.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @ClassName MockHandlerDefinition
 * @Description MockHandler定义信息
 * @Author Silwings
 * @Date 2022/11/9 23:22
 * @Since
 **/
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MockHandlerDto {

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
     * 自定义空间
     */
    private Map<String, Object> customizeSpace;

    /**
     * 检查信息
     */
    private CheckInfoDto checkInfo;

    /**
     * 响应信息集
     */
    private List<MockResponseInfoDto> responses;

    /**
     * 任务集
     */
    private List<TaskInfoDto> tasks;

    /**
     * 脚本数据集
     */
    private List<MockScriptDto> mockScriptList;

    /**
     * 用于拓展的额外数据
     */
    private Map<String, ?> extData;

    private Date updateTime;

    private long version;

    public static MockHandlerDto copyOf(final MockHandlerDto dto, final BiConsumer<MockHandlerDto, MockHandlerDtoBuilder> builderConsumer) {

        final MockHandlerDtoBuilder handlerBuilder = MockHandlerDto.builder()
                .projectId(dto.getProjectId())
                .handlerId(dto.getHandlerId())
                .enableStatus(dto.getEnableStatus())
                .name(dto.getName())
                .httpMethods(dto.getHttpMethods())
                .requestUri(dto.getRequestUri())
                .label(dto.getLabel())
                .delayTime(dto.getDelayTime())
                .checkInfo(dto.getCheckInfo())
                .customizeSpace(dto.getCustomizeSpace())
                .responses(dto.getResponses())
                .tasks(dto.getTasks())
                .updateTime(dto.getUpdateTime())
                .version(dto.getVersion());

        builderConsumer.accept(dto, handlerBuilder);

        return handlerBuilder.build();
    }

}