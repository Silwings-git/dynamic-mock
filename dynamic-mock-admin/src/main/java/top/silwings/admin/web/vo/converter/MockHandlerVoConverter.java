package top.silwings.admin.web.vo.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import top.silwings.admin.web.vo.MockHandlerInfoResultVo;
import top.silwings.admin.web.vo.MockHandlerInfoVo;
import top.silwings.admin.web.vo.MockResponseInfoVo;
import top.silwings.admin.web.vo.MockResponseVo;
import top.silwings.admin.web.vo.TaskInfoVo;
import top.silwings.admin.web.vo.TaskRequestInfoVo;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;
import top.silwings.core.model.dto.MockHandlerDto;
import top.silwings.core.model.dto.MockResponseDto;
import top.silwings.core.model.dto.MockResponseInfoDto;
import top.silwings.core.model.dto.TaskInfoDto;
import top.silwings.core.model.dto.TaskRequestDto;
import top.silwings.core.utils.ConvertUtils;

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

    public MockHandlerDto convert(final MockHandlerInfoVo vo) {
        return MockHandlerDto.builder()
                .handlerId(StringUtils.isBlank(vo.getHandlerId()) ? null : Identity.from(Long.parseLong(vo.getHandlerId())))
                .enableStatus(EnableStatus.DISABLE)
                .name(vo.getName())
                .httpMethods(vo.getHttpMethods().stream().map(method -> HttpMethod.resolve(method.toUpperCase())).collect(Collectors.toList()))
                .requestUri(vo.getRequestUri())
                .label(vo.getLabel())
                .delayTime(vo.getDelayTime())
                .customizeSpace(vo.getCustomizeSpace())
                .responses(vo.getResponses().stream().map(this::convert).collect(Collectors.toList()))
                .tasks(vo.getTasks().stream().map(this::convert).collect(Collectors.toList()))
                .build();
    }

    public MockHandlerInfoResultVo convert(final MockHandlerDto dto) {

        final MockHandlerInfoResultVo resultVo = new MockHandlerInfoResultVo();

        resultVo.setEnableStatus(dto.getEnableStatus().code());
        resultVo.setHandlerId(dto.getHandlerId().stringValue());
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

    private TaskInfoDto convert(final TaskInfoVo vo) {
        return TaskInfoDto.builder()
                .name(vo.getName())
                .support(vo.getSupport())
                .async(vo.getAsync())
                .cron(vo.getCron())
                .numberOfExecute(vo.getNumberOfExecute())
                .request(this.convert(vo.getRequest()))
                .build();
    }

    private TaskRequestDto convert(final TaskRequestInfoVo vo) {
        return TaskRequestDto.builder()
                .requestUrl(vo.getRequestUrl())
                .httpMethod(HttpMethod.resolve(vo.getHttpMethod()))
                .headers(vo.getHeaders())
                .body(vo)
                .uriVariables(vo.getUriVariables())
                .build();
    }

    private MockResponseInfoDto convert(final MockResponseInfoVo vo) {
        return MockResponseInfoDto.builder()
                .name(vo.getName())
                .support(vo.getSupport())
                .delayTime(vo.getDelayTime())
                .response(this.convert(vo.getResponse()))
                .build();
    }

    private MockResponseDto convert(final MockResponseVo vo) {
        return MockResponseDto.builder()
                .status(vo.getStatus())
                .headers(vo.getHeaders())
                .body(vo.getBody())
                .build();
    }

    private TaskInfoVo convert(final TaskInfoDto dto) {
        return TaskInfoVo.builder()
                .name(dto.getName())
                .support(dto.getSupport())
                .async(dto.isAsync())
                .cron(dto.getCron())
                .numberOfExecute(dto.getNumberOfExecute())
                .request(this.convert(dto.getRequest()))
                .build();
    }

    private TaskRequestInfoVo convert(final TaskRequestDto dto) {
        return TaskRequestInfoVo.builder()
                .requestUrl(dto.getRequestUrl())
                .httpMethod(ConvertUtils.getNoNullOrDefault(dto.getHttpMethod(), null, HttpMethod::name))
                .headers(dto.getHeaders())
                .body(dto.getBody())
                .uriVariables(dto.getUriVariables())
                .build();
    }

    private MockResponseInfoVo convert(final MockResponseInfoDto dto) {
        return MockResponseInfoVo.builder()
                .name(dto.getName())
                .support(dto.getSupport())
                .delayTime(dto.getDelayTime())
                .response(this.convert(dto.getResponse()))
                .build();
    }

    private MockResponseVo convert(final MockResponseDto dto) {
        return MockResponseVo.builder()
                .status(dto.getStatus())
                .headers(dto.getHeaders())
                .body(dto.getBody())
                .build();
    }

}