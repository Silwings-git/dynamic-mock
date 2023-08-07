package top.silwings.admin.repository.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;
import top.silwings.admin.repository.po.MockHandlerTaskRequestPo;
import top.silwings.core.common.Identity;
import top.silwings.core.model.TaskRequestDto;
import top.silwings.core.utils.JsonUtils;

import java.util.List;
import java.util.Map;

/**
 * @ClassName MockHandlerTaskRequestDaoConverter
 * @Description
 * @Author Silwings
 * @Date 2023/8/7 16:00
 * @Since
 **/
@Component
public class MockHandlerTaskRequestDaoConverter {
    public TaskRequestDto convert(final MockHandlerTaskRequestPo taskRequestPo) {
        return TaskRequestDto.builder()
                .requestUrl(taskRequestPo.getRequestUrl())
                .httpMethod(taskRequestPo.getHttpMethod())
                .headers(JsonUtils.nativeRead(taskRequestPo.getHeaders(), new TypeReference<Map<String, List<String>>>() {
                }))
                .body(JsonUtils.toBean(taskRequestPo.getBody()))
                .uriVariables(JsonUtils.nativeRead(taskRequestPo.getUriVariables(), new TypeReference<Map<String, List<String>>>() {
                }))
                .build();
    }

    public MockHandlerTaskRequestPo convert(final Identity handlerId, final TaskRequestDto taskRequestDto) {

        final MockHandlerTaskRequestPo po = new MockHandlerTaskRequestPo();
        po.setTaskRequestId(null);
        po.setHandlerId(handlerId.intValue());
        po.setTaskId(null);
        po.setRequestUrl(taskRequestDto.getRequestUrl());
        po.setHttpMethod(taskRequestDto.getHttpMethod());
        po.setHeaders(JsonUtils.toJSONString(taskRequestDto.getHeaders()));
        po.setBody(JsonUtils.toJSONString(taskRequestDto.getBody()));
        po.setUriVariables(JsonUtils.toJSONString(taskRequestDto.getUriVariables()));

        return po;
    }
}