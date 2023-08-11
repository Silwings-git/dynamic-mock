package top.silwings.admin.data_migration;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;
import top.silwings.admin.auth.annotation.PermissionLimit;
import top.silwings.admin.common.Result;
import top.silwings.admin.data_migration.mapper.DataMigrationMapper;
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

import java.util.ArrayList;
import java.util.Arrays;
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
    public Result<List<String>> dataMigration() {

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

        final List<String> migrationHandlerIdList = handlerPoList.stream()
                .map(DataMigrationMockHandlerPo::getHandlerId)
                .map(String::valueOf)
                .collect(Collectors.toList());
        migrationHandlerIdList.removeAll(failHandlerIdList.stream().map(Identity::stringValue).collect(Collectors.toList()));

        return Result.ok(migrationHandlerIdList);
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
                .customizeSpace(JsonUtils.toMap(ConvertUtils.getNoBlankOrDefault(mockHandlerPo.getCustomizeSpace(), JsonUtils.EMPTY_JSON), String.class, Object.class))
                .responses(JsonUtils.toList(ConvertUtils.getNoBlankOrDefault(mockHandlerPo.getResponses(), JsonUtils.EMPTY_ARRAY), MockResponseInfoDto.class))
                .tasks(JsonUtils.toList(ConvertUtils.getNoBlankOrDefault(mockHandlerPo.getTasks(), JsonUtils.EMPTY_ARRAY), TaskInfoDto.class))
                .updateTime(mockHandlerPo.getUpdateTime())
                .build();
    }

}