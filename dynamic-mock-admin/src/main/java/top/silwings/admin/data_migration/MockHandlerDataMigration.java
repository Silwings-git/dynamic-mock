package top.silwings.admin.data_migration;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;
import top.silwings.admin.auth.annotation.PermissionLimit;
import top.silwings.admin.common.DynamicMockBaseMapper;
import top.silwings.admin.common.Result;
import top.silwings.admin.web.controller.MockHandlerController;
import top.silwings.admin.web.vo.converter.MockHandlerVoConverter;
import top.silwings.admin.web.vo.param.MockHandlerInfoParam;
import top.silwings.admin.web.vo.result.MockHandlerInfoResult;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;
import top.silwings.core.model.MockHandlerDto;
import top.silwings.core.model.MockResponseInfoDto;
import top.silwings.core.model.TaskInfoDto;
import top.silwings.core.utils.ConvertUtils;
import top.silwings.core.utils.JsonUtils;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerDataMigration
 * @Description
 * @Author Silwings
 * @Date 2023/8/9 10:27
 * @Since
 **/
@Slf4j
@RestController
@RequestMapping("/dynamic-mock/mockHandlerDataMigration")
public class MockHandlerDataMigration {

    private final DataMigrationMapper dataMigrationMapper;
    private final MockHandlerVoConverter mockHandlerVoConverter;

    private final MockHandlerController mockHandlerController;

    public MockHandlerDataMigration(final DataMigrationMapper dataMigrationMapper, final MockHandlerVoConverter mockHandlerVoConverter, final MockHandlerController mockHandlerController) {
        this.dataMigrationMapper = dataMigrationMapper;
        this.mockHandlerVoConverter = mockHandlerVoConverter;
        this.mockHandlerController = mockHandlerController;
    }

    /**
     * 仅对response,condition,task中未出现的handlerId进行迁移
     *
     * @return
     */
    @PostMapping("/migration")
    @PermissionLimit
    public Result<Void> dataMigration() {

        final List<String> handlerIds = this.dataMigrationMapper.queryHandlerIdInResponse();
        handlerIds.addAll(this.dataMigrationMapper.queryHandlerIdInCondition());
        handlerIds.addAll(this.dataMigrationMapper.queryHandlerIdInTask());

        final List<String> handlerIdList = handlerIds.stream().distinct().collect(Collectors.toList());

        final List<DataMigrationMockHandlerPo> handlerPoList = this.queryHandlerNotIn(handlerIdList);

        final List<MockHandlerInfoParam> paramList = handlerPoList
                .stream()
                .map(this::convert)
                .map(e -> this.mockHandlerVoConverter.convert(e, null))
                .map(this::handlerInfoResult2Param)
                .collect(Collectors.toList());


        final List<Identity> failHandlerIdList = new ArrayList<>();
        for (final MockHandlerInfoParam param : paramList) {
            try {
                this.mockHandlerController.save(param);
            } catch (Exception e) {
                log.error("数据迁移失败 {}.", param.getHandlerId(), e);
                failHandlerIdList.add(param.getHandlerId());
            }
        }

        if (CollectionUtils.isNotEmpty(failHandlerIdList)) {
            log.error("失败的handlerId: {}", JsonUtils.toJSONString(failHandlerIdList));
        }

        return Result.ok();
    }

    @SuppressWarnings("unchecked")
    private MockHandlerInfoParam handlerInfoResult2Param(final MockHandlerInfoResult mockHandlerInfoResult) {

        final MockHandlerInfoParam param = new MockHandlerInfoParam();
        param.setProjectId(mockHandlerInfoResult.getProjectId());
        param.setHandlerId(mockHandlerInfoResult.getHandlerId());
        param.setName(mockHandlerInfoResult.getName());
        param.setHttpMethods(mockHandlerInfoResult.getHttpMethods());
        param.setRequestUri(mockHandlerInfoResult.getRequestUri());
        param.setLabel(mockHandlerInfoResult.getLabel());
        param.setDelayTime(mockHandlerInfoResult.getDelayTime());
        param.setCustomizeSpace((Map<String, Object>) mockHandlerInfoResult.getCustomizeSpace());
        param.setResponses(mockHandlerInfoResult.getResponses());
        param.setTasks(mockHandlerInfoResult.getTasks());

        return param;
    }

    private List<DataMigrationMockHandlerPo> queryHandlerNotIn(final List<String> handlerIdList) {

        final Example example = new Example(DataMigrationMockHandlerPo.class);

        if (CollectionUtils.isNotEmpty(handlerIdList)) {
            example.createCriteria()
                    .andNotIn(DataMigrationMockHandlerPo.C_HANDLER_ID, handlerIdList);
        }

        return this.dataMigrationMapper.selectByCondition(example);
    }


    public MockHandlerDto convert(final DataMigrationMockHandlerPo mockHandlerPo) {

        return MockHandlerDto.builder()
                .handlerId(Identity.from(mockHandlerPo.getHandlerId()))
                .projectId(Identity.from(mockHandlerPo.getProjectId()))
                .enableStatus(ConvertUtils.getNoNullOrDefault(mockHandlerPo.getEnableStatus(), EnableStatus.DISABLE, EnableStatus::valueOfCode))
                .name(mockHandlerPo.getName())
                .httpMethods(Arrays.stream(mockHandlerPo.getHttpMethods().split(",")).map(HttpMethod::resolve).collect(Collectors.toList()))
                .requestUri(mockHandlerPo.getRequestUri())
                .label(mockHandlerPo.getLabel())
                .delayTime(mockHandlerPo.getDelayTime())
                .customizeSpace(JsonUtils.toMap(ConvertUtils.getNoBlankOrDefault(mockHandlerPo.getCustomizeSpace(), "{}"), String.class, Object.class))
                .responses(JsonUtils.toList(ConvertUtils.getNoBlankOrDefault(mockHandlerPo.getResponses(), "[]"), MockResponseInfoDto.class))
                .tasks(JsonUtils.toList(ConvertUtils.getNoBlankOrDefault(mockHandlerPo.getTasks(), "[]"), TaskInfoDto.class))
                .updateTime(mockHandlerPo.getUpdateTime())
                .build();
    }

    @Mapper
    public interface DataMigrationMapper extends DynamicMockBaseMapper<DataMigrationMockHandlerPo> {

        @Select("SELECT DISTINCT handler_id FROM dm_mock_handler_response")
        List<String> queryHandlerIdInResponse();

        @Select("SELECT DISTINCT handler_id FROM dm_mock_handler_condition")
        List<String> queryHandlerIdInCondition();

        @Select("SELECT DISTINCT handler_id FROM dm_mock_handler_task")
        List<String> queryHandlerIdInTask();
    }

    @Getter
    @Setter
    @Table(name = "dm_mock_handler")
    public static class DataMigrationMockHandlerPo {

        public static final String C_HANDLER_ID = "handlerId";

        /**
         * 唯一标识
         */
        @Id
        @GeneratedValue(generator = "JDBC")
        private Integer handlerId;

        /**
         * 所属项目id
         */
        @Column(name = "project_id")
        private Integer projectId;

        /**
         * 启用状态.
         * {@link top.silwings.core.common.EnableStatus}
         */
        @Column(name = "enable_status")
        private Integer enableStatus;

        /**
         * 处理器名称
         */
        @Column(name = "name")
        private String name;

        /**
         * 支持的请求方式
         */
        @Column(name = "http_methods")
        private String httpMethods;

        /**
         * 支持的请求URI
         */
        @Column(name = "request_uri")
        private String requestUri;

        /**
         * 自定义标签
         */
        @Column(name = "label")
        private String label;

        /**
         * 延迟执行时间
         */
        @Column(name = "delay_time")
        private Integer delayTime;

        /**
         * 自定义参数空间
         */
        @Column(name = "customize_space")
        private String customizeSpace;

        /**
         * 模拟响应信息
         */
        @Column(name = "responses")
        private String responses;

        /**
         * 任务集
         */
        @Column(name = "tasks")
        private String tasks;

        /**
         * 负责人
         */
        @Column(name = "author")
        private String author;

        /**
         * 创建时间
         */
        @Column(name = "create_time")
        private Date createTime;

        /**
         * 更新时间
         */
        @Column(name = "update_time")
        private Date updateTime;
    }

}