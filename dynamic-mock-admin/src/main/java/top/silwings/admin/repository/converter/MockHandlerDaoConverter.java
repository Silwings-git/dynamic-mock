package top.silwings.admin.repository.converter;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import top.silwings.admin.repository.po.MockHandlerPo;
import top.silwings.admin.repository.po.pack.MockHandlerPoWrap;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;
import top.silwings.core.model.MockHandlerDto;
import top.silwings.core.model.MockResponseInfoDto;
import top.silwings.core.model.TaskInfoDto;
import top.silwings.core.utils.ConvertUtils;
import top.silwings.core.utils.JsonUtils;

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
        mockHandlerPo.setCustomizeSpace(JsonUtils.toJSONString(ConvertUtils.getNoNullOrDefault(mockHandlerDto.getCustomizeSpace(), "{}")));

        final MockHandlerPoWrap handlerPoWrap = new MockHandlerPoWrap();
        handlerPoWrap.setMockHandlerPo(mockHandlerPo);
        handlerPoWrap.setMockHandlerTaskPoWrapList(this.mockHandlerTaskDaoConverter.listConvert(mockHandlerDto.getHandlerId(), mockHandlerDto.getTasks()));
        handlerPoWrap.setMockHandlerResponsePoWrapList(this.mockHandlerResponseDaoConverter.listConvert(mockHandlerDto.getHandlerId(), mockHandlerDto.getResponses()));

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
                .customizeSpace(JsonUtils.toMap(ConvertUtils.getNoBlankOrDefault(mockHandlerPo.getCustomizeSpace(), "{}"), String.class, Object.class))
                .responses(ConvertUtils.getNoNullOrDefault(mockResponseInfoDtoList, Collections::emptyList))
                .tasks(ConvertUtils.getNoNullOrDefault(taskInfoDtoList, Collections::emptyList))
                .updateTime(mockHandlerPo.getUpdateTime())
                .build();
    }

}