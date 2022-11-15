package top.silwings.admin.web.vo.converter;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import top.silwings.core.repository.dto.MockHandlerDto;
import top.silwings.core.repository.dto.MockResponseDto;
import top.silwings.core.repository.dto.MockResponseInfoDto;
import top.silwings.core.repository.dto.TaskInfoDto;
import top.silwings.core.repository.dto.TaskRequestDto;
import top.silwings.core.utils.ConvertUtils;
import top.silwings.admin.web.vo.MockHandlerInfoVo;
import top.silwings.admin.web.vo.MockResponseInfoVo;
import top.silwings.admin.web.vo.MockResponseVo;
import top.silwings.admin.web.vo.TaskInfoVo;
import top.silwings.admin.web.vo.TaskRequestInfoVo;

import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerConverter
 * @Description
 * @Author Silwings
 * @Date 2022/11/14 22:38
 * @Since
 **/
@Component
public class MockHandlerConverter {

    public MockHandlerDto convert(final MockHandlerInfoVo vo) {
        final MockHandlerDto dto = MockHandlerDto.builder()
                .name(vo.getName())
                .httpMethods(vo.getHttpMethods().stream().map(method -> HttpMethod.resolve(method.toUpperCase())).collect(Collectors.toList()))
                .requestUri(vo.getRequestUri())
                .label(vo.getLabel())
                .delayTime(vo.getDelayTime())
                .customizeSpace(vo.getCustomizeSpace())
                .responses(vo.getResponses().stream().map(this::convert).collect(Collectors.toList()))
                .tasks(vo.getTasks().stream().map(this::convert).collect(Collectors.toList()))
                .build();
        dto.setId(vo.getId());
        return dto;
    }

    public MockHandlerInfoVo convert(final MockHandlerDto dto) {
        final MockHandlerInfoVo vo = MockHandlerInfoVo.builder()
                .name(dto.getName())
                .httpMethods(dto.getHttpMethods().stream().map(HttpMethod::name).collect(Collectors.toList()))
                .requestUri(dto.getRequestUri())
                .label(dto.getLabel())
                .delayTime(dto.getDelayTime())
                .customizeSpace(dto.getCustomizeSpace())
                .responses(dto.getResponses().stream().map(this::convert).collect(Collectors.toList()))
                .tasks(dto.getTasks().stream().map(this::convert).collect(Collectors.toList()))
                .build();
        vo.setId(dto.getId());
        return vo;
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