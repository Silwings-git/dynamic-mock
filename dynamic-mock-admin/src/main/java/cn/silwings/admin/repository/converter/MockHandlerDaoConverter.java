package cn.silwings.admin.repository.converter;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import cn.silwings.admin.model.MockHandlerSummaryDto;
import cn.silwings.admin.repository.po.MockHandlerPo;
import cn.silwings.admin.repository.po.pack.MockHandlerPoWrap;
import cn.silwings.core.common.EnableStatus;
import cn.silwings.core.common.Identity;
import cn.silwings.core.model.CheckInfoDto;
import cn.silwings.core.model.MockHandlerDto;
import cn.silwings.core.handler.plugin.MockHandlerPluginInfo;
import cn.silwings.core.model.MockResponseInfoDto;
import cn.silwings.core.model.TaskInfoDto;
import cn.silwings.core.utils.ConvertUtils;
import cn.silwings.core.utils.JsonUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerDaoConverter
 * @Description
 * @Author Silwings
 * @Date 2022/11/15 23:00
 * @Since
 **/
@Component
public class MockHandlerDaoConverter {

    private final MockHandlerTaskDaoConverter mockHandlerTaskDaoConverter;

    private final MockHandlerResponseDaoConverter mockHandlerResponseDaoConverter;

    public MockHandlerDaoConverter(final MockHandlerTaskDaoConverter mockHandlerTaskDaoConverter, final MockHandlerResponseDaoConverter mockHandlerResponseDaoConverter) {
        this.mockHandlerTaskDaoConverter = mockHandlerTaskDaoConverter;
        this.mockHandlerResponseDaoConverter = mockHandlerResponseDaoConverter;
    }

    public MockHandlerPoWrap convert(final MockHandlerDto mockHandlerDto) {

        final MockHandlerPo mockHandlerPo = new MockHandlerPo();
        mockHandlerPo.setHandlerId(ConvertUtils.getNoNullOrDefault(mockHandlerDto.getHandlerId(), null, Identity::intValue));
        mockHandlerPo.setProjectId(mockHandlerDto.getProjectId().intValue());
        mockHandlerPo.setEnableStatus(ConvertUtils.getNoNullOrDefault(mockHandlerDto.getEnableStatus(), EnableStatus.DISABLE.code(), EnableStatus::code));
        mockHandlerPo.setName(mockHandlerDto.getName());
        if (CollectionUtils.isNotEmpty(mockHandlerDto.getHttpMethods())) {
            mockHandlerPo.setHttpMethods(mockHandlerDto.getHttpMethods().stream().map(HttpMethod::name).collect(Collectors.joining(",")));
        }
        mockHandlerPo.setRequestUri(mockHandlerDto.getRequestUri());
        mockHandlerPo.setLabel(mockHandlerDto.getLabel());
        mockHandlerPo.setDelayTime(mockHandlerDto.getDelayTime());
        mockHandlerPo.setCustomizeSpace(JsonUtils.toJSONString(ConvertUtils.getNoNullOrDefault(mockHandlerDto.getCustomizeSpace(), JsonUtils.EMPTY_OBJECT)));
        mockHandlerPo.setCheckInfoJson(JsonUtils.toJSONString(ConvertUtils.getNoNullOrDefault(mockHandlerDto.getCheckInfo(), JsonUtils.EMPTY_OBJECT)));
        mockHandlerPo.setPluginInfosJson(JsonUtils.toJSONString(ConvertUtils.getNoNullOrDefault(mockHandlerDto.getPlugins(), JsonUtils.EMPTY_ARRAY)));

        final MockHandlerPoWrap handlerPoWrap = new MockHandlerPoWrap();
        handlerPoWrap.setMockHandlerPo(mockHandlerPo);
        handlerPoWrap.setMockHandlerResponsePoWrapList(this.mockHandlerResponseDaoConverter.listConvert(mockHandlerDto.getHandlerId(), mockHandlerDto.getResponses()));
        handlerPoWrap.setMockHandlerTaskPoWrapList(this.mockHandlerTaskDaoConverter.listConvert(mockHandlerDto.getHandlerId(), mockHandlerDto.getTasks()));

        return handlerPoWrap;
    }

    public MockHandlerDto convert(final MockHandlerPo mockHandlerPo, final List<MockResponseInfoDto> mockResponseInfoDtoList, final List<TaskInfoDto> taskInfoDtoList) {

        return MockHandlerDto.builder()
                .handlerId(Identity.from(mockHandlerPo.getHandlerId()))
                .projectId(Identity.from(mockHandlerPo.getProjectId()))
                .enableStatus(ConvertUtils.getNoNullOrDefault(mockHandlerPo.getEnableStatus(), EnableStatus.DISABLE, EnableStatus::valueOfCode))
                .name(mockHandlerPo.getName())
                .httpMethods(Arrays.stream(mockHandlerPo.getHttpMethods().split(",")).map(HttpMethod::resolve).collect(Collectors.toList()))
                .requestUri(mockHandlerPo.getRequestUri())
                .label(mockHandlerPo.getLabel())
                .delayTime(mockHandlerPo.getDelayTime())
                .customizeSpace(JsonUtils.toMap(ConvertUtils.getNoBlankOrDefault(mockHandlerPo.getCustomizeSpace(), JsonUtils.EMPTY_OBJECT), String.class, Object.class))
                .checkInfo(JsonUtils.tryToBean(mockHandlerPo.getCheckInfoJson(), CheckInfoDto.class, CheckInfoDto::newEmpty))
                .plugins(JsonUtils.tryToList(mockHandlerPo.getPluginInfosJson(), MockHandlerPluginInfo.class, Collections::emptyList))
                .responses(ConvertUtils.getNoNullOrDefault(mockResponseInfoDtoList, Collections::emptyList))
                .tasks(ConvertUtils.getNoNullOrDefault(taskInfoDtoList, Collections::emptyList))
                .updateTime(mockHandlerPo.getUpdateTime())
                .version(mockHandlerPo.getIncrementVersion())
                .build();
    }

    public MockHandlerSummaryDto convertSummary(final MockHandlerPo mockHandlerPo) {
        return MockHandlerSummaryDto.builder()
                .handlerId(Identity.from(mockHandlerPo.getHandlerId()))
                .projectId(Identity.from(mockHandlerPo.getProjectId()))
                .enableStatus(ConvertUtils.getNoNullOrDefault(mockHandlerPo.getEnableStatus(), EnableStatus.DISABLE, EnableStatus::valueOfCode))
                .name(mockHandlerPo.getName())
                .httpMethods(Arrays.stream(mockHandlerPo.getHttpMethods().split(",")).map(HttpMethod::resolve).collect(Collectors.toList()))
                .requestUri(mockHandlerPo.getRequestUri())
                .label(mockHandlerPo.getLabel())
                .delayTime(mockHandlerPo.getDelayTime())
                .updateTime(mockHandlerPo.getUpdateTime())
                .build();
    }
}