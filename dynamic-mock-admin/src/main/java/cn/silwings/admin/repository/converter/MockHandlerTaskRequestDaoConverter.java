package cn.silwings.admin.repository.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;
import cn.silwings.admin.repository.po.MockHandlerTaskRequestPo;
import cn.silwings.core.common.Identity;
import cn.silwings.core.model.TaskRequestDto;
import cn.silwings.core.utils.ConvertUtils;
import cn.silwings.core.utils.JsonUtils;

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
                .headers(JsonUtils.nativeRead(ConvertUtils.getNoBlankOrDefault(taskRequestPo.getHeaders(), JsonUtils.EMPTY_OBJECT), new TypeReference<Map<String, List<String>>>() {
                }))
                .body(JsonUtils.tryToBean(taskRequestPo.getBody()))
                .uriVariables(JsonUtils.nativeRead(ConvertUtils.getNoBlankOrDefault(taskRequestPo.getUriVariables(), JsonUtils.EMPTY_OBJECT), new TypeReference<Map<String, List<String>>>() {
                }))
                .build();
    }

    public MockHandlerTaskRequestPo convert(final Identity handlerId, final TaskRequestDto taskRequestDto) {

        final MockHandlerTaskRequestPo po = new MockHandlerTaskRequestPo();
        po.setTaskRequestId(null);
        po.setHandlerId(ConvertUtils.getNoNullOrDefault(handlerId, null, Identity::intValue));
        po.setTaskId(null);
        po.setRequestUrl(taskRequestDto.getRequestUrl());
        po.setHttpMethod(taskRequestDto.getHttpMethod());
        po.setHeaders(JsonUtils.toJSONString(taskRequestDto.getHeaders()));
        po.setBody(JsonUtils.toJSONString(taskRequestDto.getBody()));
        po.setUriVariables(JsonUtils.toJSONString(taskRequestDto.getUriVariables()));

        return po;
    }
}