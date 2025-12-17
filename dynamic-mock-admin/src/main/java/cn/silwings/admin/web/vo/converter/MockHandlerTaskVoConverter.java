package cn.silwings.admin.web.vo.converter;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import cn.silwings.admin.web.vo.param.SaveTaskInfoParam;
import cn.silwings.admin.web.vo.param.SaveTaskRequestInfoParam;
import cn.silwings.core.common.EnableStatus;
import cn.silwings.core.model.TaskInfoDto;
import cn.silwings.core.model.TaskRequestDto;
import cn.silwings.core.utils.ConvertUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerTaskVoConverter
 * @Description
 * @Author Silwings
 * @Date 2023/8/13 13:06
 * @Since
 **/
@Component
public class MockHandlerTaskVoConverter {

    public List<TaskInfoDto> convert(final List<SaveTaskInfoParam> voList) {
        if (CollectionUtils.isEmpty(voList)) {
            return Collections.emptyList();
        }
        return voList.stream().map(this::convert).collect(Collectors.toList());
    }

    public TaskInfoDto convert(final SaveTaskInfoParam vo) {
        return TaskInfoDto.builder()
                .taskId(vo.getTaskId())
                .name(vo.getName())
                .enableStatus(EnableStatus.valueOfCode(vo.getEnableStatus()))
                .support(vo.getSupport())
                .async(ConvertUtils.getNoNullOrDefault(vo.getAsync(), false))
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
                .body(vo.getBody())
                .uriVariables(vo.getUriVariables())
                .build();
    }

    public SaveTaskInfoParam convert(final TaskInfoDto dto) {
        return SaveTaskInfoParam.builder()
                .taskId(dto.getTaskId())
                .name(dto.getName())
                .enableStatus(dto.getEnableStatus().code())
                .support(dto.getSupport())
                .async(dto.isAsync())
                .cron(dto.getCron())
                .numberOfExecute(dto.getNumberOfExecute())
                .request(ConvertUtils.getNoNullOrDefault(dto.getRequest(), null, this::convert))
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

    public List<SaveTaskInfoParam> convert2Param(final List<TaskInfoDto> taskInfoList) {
        if (CollectionUtils.isEmpty(taskInfoList)) {
            return Collections.emptyList();
        }
        return taskInfoList.stream().map(this::convert).collect(Collectors.toList());
    }
}