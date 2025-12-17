package cn.silwings.admin.web.vo.converter;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import cn.silwings.admin.model.MockHandlerSummaryDto;
import cn.silwings.admin.model.ProjectDto;
import cn.silwings.admin.web.vo.param.MockHandlerInfoParam;
import cn.silwings.admin.web.vo.result.MockHandlerInfoResult;
import cn.silwings.admin.web.vo.result.MockHandlerSummaryResult;
import cn.silwings.core.common.EnableStatus;
import cn.silwings.core.model.MockHandlerDto;
import cn.silwings.core.utils.ConvertUtils;

import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerConverter
 * @Description
 * @Author Silwings
 * @Date 2022/11/14 22:38
 * @Since
 **/
@Component
public class MockHandlerVoConverter {

    private final MockHandlerResponseVoConverter mockHandlerResponseVoConverter;

    private final MockHandlerCheckVoConverter mockHandlerCheckVoConverter;

    private final MockHandlerTaskVoConverter mockHandlerTaskVoConverter;

    private final MockHandlerPluginVoConverter mockHandlerPluginVoConverter;

    public MockHandlerVoConverter(final MockHandlerResponseVoConverter mockHandlerResponseVoConverter, final MockHandlerCheckVoConverter mockHandlerCheckVoConverter, final MockHandlerTaskVoConverter mockHandlerTaskVoConverter, final MockHandlerPluginVoConverter mockHandlerPluginVoConverter) {
        this.mockHandlerResponseVoConverter = mockHandlerResponseVoConverter;
        this.mockHandlerCheckVoConverter = mockHandlerCheckVoConverter;
        this.mockHandlerTaskVoConverter = mockHandlerTaskVoConverter;
        this.mockHandlerPluginVoConverter = mockHandlerPluginVoConverter;
    }

    public MockHandlerDto convert(final MockHandlerInfoParam vo) {
        final String requestUri = vo.getRequestUri();
        return MockHandlerDto.builder()
                .projectId(vo.getProjectId())
                .handlerId(vo.getHandlerId())
                .enableStatus(EnableStatus.DISABLE)
                .name(vo.getName())
                .httpMethods(vo.getHttpMethods().stream().map(method -> HttpMethod.resolve(method.toUpperCase())).filter(Objects::nonNull).collect(Collectors.toList()))
                .requestUri(requestUri.startsWith("/") ? requestUri : "/" + requestUri)
                .label(vo.getLabel())
                .delayTime(ConvertUtils.getNoNullOrDefault(vo.getDelayTime(), 0))
                .customizeSpace(vo.getCustomizeSpace())
                .checkInfo(this.mockHandlerCheckVoConverter.convert(vo.getCheckInfo()))
                .responses(this.mockHandlerResponseVoConverter.convert(vo.getResponses()))
                .tasks(this.mockHandlerTaskVoConverter.convert(vo.getTasks()))
                .plugins(this.mockHandlerPluginVoConverter.convert(vo.getPluginInfos()))
                .updateTime(new Date())
                .build();
    }

    public MockHandlerInfoResult convert(final MockHandlerDto dto, final ProjectDto project) {

        final MockHandlerInfoResult resultVo = new MockHandlerInfoResult();

        resultVo.setProjectId(dto.getProjectId());
        resultVo.setProjectName(ConvertUtils.getNoNullOrDefault(project, null, ProjectDto::getProjectName));
        resultVo.setBaseUri(ConvertUtils.getNoNullOrDefault(project, null, ProjectDto::getBaseUri));
        resultVo.setEnableStatus(dto.getEnableStatus().code());
        resultVo.setHandlerId(dto.getHandlerId());
        resultVo.setName(dto.getName());
        resultVo.setHttpMethods(dto.getHttpMethods().stream().map(HttpMethod::name).collect(Collectors.toList()));
        resultVo.setRequestUri(dto.getRequestUri());
        resultVo.setLabel(dto.getLabel());
        resultVo.setDelayTime(dto.getDelayTime());
        resultVo.setCustomizeSpace(dto.getCustomizeSpace());
        resultVo.setCheckInfo(this.mockHandlerCheckVoConverter.convert(dto.getCheckInfo()));
        resultVo.setPluginInfos(this.mockHandlerPluginVoConverter.convert2Param(dto.getPlugins()));
        resultVo.setResponses(this.mockHandlerResponseVoConverter.convert2Param(dto.getResponses()));
        resultVo.setTasks(this.mockHandlerTaskVoConverter.convert2Param(dto.getTasks()));
        resultVo.setUpdateTime(dto.getUpdateTime());

        return resultVo;
    }

    public MockHandlerSummaryResult convertSummary(final MockHandlerSummaryDto handlerSummary, final ProjectDto project) {

        final MockHandlerSummaryResult resultVo = new MockHandlerSummaryResult();
        resultVo.setProjectId(handlerSummary.getProjectId());
        resultVo.setProjectName(ConvertUtils.getNoNullOrDefault(project, null, ProjectDto::getProjectName));
        resultVo.setBaseUri(ConvertUtils.getNoNullOrDefault(project, null, ProjectDto::getBaseUri));
        resultVo.setEnableStatus(handlerSummary.getEnableStatus().code());
        resultVo.setHandlerId(handlerSummary.getHandlerId());
        resultVo.setName(handlerSummary.getName());
        resultVo.setHttpMethods(handlerSummary.getHttpMethods().stream().map(HttpMethod::name).collect(Collectors.toList()));
        resultVo.setRequestUri(handlerSummary.getRequestUri());
        resultVo.setLabel(handlerSummary.getLabel());
        resultVo.setUpdateTime(handlerSummary.getUpdateTime());

        return resultVo;
    }
}