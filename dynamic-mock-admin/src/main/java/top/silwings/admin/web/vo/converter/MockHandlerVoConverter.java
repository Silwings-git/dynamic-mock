package top.silwings.admin.web.vo.converter;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import top.silwings.admin.web.vo.param.MockHandlerInfoParam;
import top.silwings.admin.web.vo.param.MockResponseInfoParam;
import top.silwings.admin.web.vo.param.MockResponseParam;
import top.silwings.admin.web.vo.param.SaveTaskInfoParam;
import top.silwings.admin.web.vo.param.SaveTaskRequestInfoParam;
import top.silwings.admin.web.vo.result.MockHandlerInfoResult;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.model.MockHandlerDto;
import top.silwings.core.model.MockResponseDto;
import top.silwings.core.model.MockResponseInfoDto;
import top.silwings.core.model.TaskInfoDto;
import top.silwings.core.model.TaskRequestDto;
import top.silwings.core.utils.ConvertUtils;

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
                .delayTime(vo.getDelayTime())
                .customizeSpace(vo.getCustomizeSpace())
                .responses(vo.getResponses().stream().map(this::convert).collect(Collectors.toList()))
                .tasks(vo.getTasks().stream().map(this::convert).collect(Collectors.toList()))
                .build();
    }

    public MockHandlerInfoResult convert(final MockHandlerDto dto) {

        final MockHandlerInfoResult resultVo = new MockHandlerInfoResult();

        resultVo.setProjectId(dto.getProjectId());
        resultVo.setEnableStatus(dto.getEnableStatus().code());
        resultVo.setHandlerId(dto.getHandlerId());
        resultVo.setName(dto.getName());
        resultVo.setHttpMethods(dto.getHttpMethods().stream().map(HttpMethod::name).collect(Collectors.toList()));
        resultVo.setRequestUri(dto.getRequestUri());
        resultVo.setLabel(dto.getLabel());
        resultVo.setDelayTime(dto.getDelayTime());
        resultVo.setCustomizeSpace(dto.getCustomizeSpace());
        resultVo.setResponses(dto.getResponses().stream().map(this::convert).collect(Collectors.toList()));
        resultVo.setTasks(dto.getTasks().stream().map(this::convert).collect(Collectors.toList()));
        resultVo.setUpdateTime(dto.getUpdateTime());

        return resultVo;
    }

    private TaskInfoDto convert(final SaveTaskInfoParam vo) {
        return TaskInfoDto.builder()
                .name(vo.getName())
                .support(vo.getSupport())
                .async(vo.getAsync())
                .cron(vo.getCron())
                .numberOfExecute(vo.getNumberOfExecute())
                .request(this.convert(vo.getRequest()))
                .build();
    }

    private TaskRequestDto convert(final SaveTaskRequestInfoParam vo) {
        return TaskRequestDto.builder()
                .requestUrl(vo.getRequestUrl())
                .httpMethod(HttpMethod.resolve(vo.getHttpMethod()))
                .headers(vo.getHeaders())
                .body(vo)
                .uriVariables(vo.getUriVariables())
                .build();
    }

    private MockResponseInfoDto convert(final MockResponseInfoParam vo) {
        return MockResponseInfoDto.builder()
                .name(vo.getName())
                .support(vo.getSupport())
                .delayTime(vo.getDelayTime())
                .response(this.convert(vo.getResponse()))
                .build();
    }

    private MockResponseDto convert(final MockResponseParam vo) {
        return MockResponseDto.builder()
                .status(vo.getStatus())
                .headers(vo.getHeaders())
                .body(vo.getBody())
                .build();
    }

    private SaveTaskInfoParam convert(final TaskInfoDto dto) {
        return SaveTaskInfoParam.builder()
                .name(dto.getName())
                .support(dto.getSupport())
                .async(dto.isAsync())
                .cron(dto.getCron())
                .numberOfExecute(dto.getNumberOfExecute())
                .request(this.convert(dto.getRequest()))
                .build();
    }

    private SaveTaskRequestInfoParam convert(final TaskRequestDto dto) {
        return SaveTaskRequestInfoParam.builder()
                .requestUrl(dto.getRequestUrl())
                .httpMethod(ConvertUtils.getNoNullOrDefault(dto.getHttpMethod(), null, HttpMethod::name))
                .headers(dto.getHeaders())
                .body(dto.getBody())
                .uriVariables(dto.getUriVariables())
                .build();
    }

    private MockResponseInfoParam convert(final MockResponseInfoDto dto) {
        return MockResponseInfoParam.builder()
                .name(dto.getName())
                .support(dto.getSupport())
                .delayTime(dto.getDelayTime())
                .response(this.convert(dto.getResponse()))
                .build();
    }

    private MockResponseParam convert(final MockResponseDto dto) {
        return MockResponseParam.builder()
                .status(dto.getStatus())
                .headers(dto.getHeaders())
                .body(dto.getBody())
                .build();
    }

}