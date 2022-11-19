package top.silwings.admin.repository.db.mysql.converter;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import top.silwings.admin.repository.db.mysql.po.MockHandlerPo;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;
import top.silwings.core.model.dto.MockHandlerDto;
import top.silwings.core.model.dto.MockResponseInfoDto;
import top.silwings.core.model.dto.TaskInfoDto;
import top.silwings.core.utils.ConvertUtils;
import top.silwings.core.utils.JsonUtils;

import java.util.Arrays;
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

    private static final String EMPTY_LIST = "[]";
    private static final String EMPTY_JSON = "{}";

    public MockHandlerPo convert(final MockHandlerDto mockHandlerDto) {

        final MockHandlerPo dao = new MockHandlerPo();
        dao.setHandlerId(ConvertUtils.getNoNullOrDefault(mockHandlerDto.getHandlerId(), null, Identity::longValue));
        dao.setEnableStatus(ConvertUtils.getNoNullOrDefault(mockHandlerDto.getEnableStatus(), EnableStatus.DISABLE.code(), EnableStatus::code));
        dao.setName(mockHandlerDto.getName());
        if (CollectionUtils.isNotEmpty(mockHandlerDto.getHttpMethods())) {
            dao.setHttpMethods(mockHandlerDto.getHttpMethods().stream().map(HttpMethod::name).collect(Collectors.joining(",")));
        }
        dao.setRequestUri(mockHandlerDto.getRequestUri());
        dao.setLabel(mockHandlerDto.getLabel());
        dao.setDelayTime(mockHandlerDto.getDelayTime());
        dao.setCustomizeSpace(JsonUtils.toJSONString(ConvertUtils.getNoNullOrDefault(mockHandlerDto.getCustomizeSpace(), EMPTY_JSON)));
        dao.setResponses(JsonUtils.toJSONString(ConvertUtils.getNoNullOrDefault(mockHandlerDto.getResponses(), EMPTY_LIST)));
        dao.setTasks(JsonUtils.toJSONString(ConvertUtils.getNoNullOrDefault(mockHandlerDto.getTasks(), EMPTY_LIST)));

        return dao;
    }

    public MockHandlerDto convert(final MockHandlerPo mockHandlerPo) {

        return MockHandlerDto.builder()
                .handlerId(Identity.from(mockHandlerPo.getHandlerId()))
                .enableStatus(ConvertUtils.getNoNullOrDefault(mockHandlerPo.getEnableStatus(), EnableStatus.DISABLE, EnableStatus::valueOfCode))
                .name(mockHandlerPo.getName())
                .httpMethods(Arrays.stream(mockHandlerPo.getHttpMethods().split(",")).map(HttpMethod::resolve).collect(Collectors.toList()))
                .requestUri(mockHandlerPo.getRequestUri())
                .label(mockHandlerPo.getLabel())
                .delayTime(mockHandlerPo.getDelayTime())
                .customizeSpace(JSON.parseObject(mockHandlerPo.getCustomizeSpace()))
                .responses(JSON.parseArray(mockHandlerPo.getResponses(), MockResponseInfoDto.class))
                .tasks(JSON.parseArray(mockHandlerPo.getTasks(), TaskInfoDto.class))
                .updateTime(mockHandlerPo.getUpdateTime())
                .build();
    }
}