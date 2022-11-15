package top.silwings.core.repository.db.mysql.dao.converter;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import top.silwings.core.common.Identity;
import top.silwings.core.repository.db.mysql.dao.MockHandlerDao;
import top.silwings.core.repository.dto.MockHandlerDto;
import top.silwings.core.repository.dto.MockResponseInfoDto;
import top.silwings.core.repository.dto.TaskInfoDto;
import top.silwings.core.utils.ConvertUtils;

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

    public MockHandlerDao convert(final MockHandlerDto mockHandlerDto) {

        final MockHandlerDao dao = new MockHandlerDao();
        dao.setHandlerId(mockHandlerDto.getHandlerId().getId());
        dao.setName(mockHandlerDto.getName());
        if (CollectionUtils.isNotEmpty(mockHandlerDto.getHttpMethods())) {
            dao.setHttpMethods(mockHandlerDto.getHttpMethods().stream().map(HttpMethod::name).collect(Collectors.joining(",")));
        }
        dao.setRequestUri(mockHandlerDto.getRequestUri());
        dao.setLabel(mockHandlerDto.getLabel());
        dao.setDelayTime(mockHandlerDto.getDelayTime());
        dao.setCustomizeSpace(JSON.toJSONString(ConvertUtils.getNoNullOrDefault(mockHandlerDto.getCustomizeSpace(), EMPTY_JSON)));
        dao.setResponses(JSON.toJSONString(ConvertUtils.getNoNullOrDefault(mockHandlerDto.getResponses(), EMPTY_LIST)));
        dao.setTasks(JSON.toJSONString(ConvertUtils.getNoNullOrDefault(mockHandlerDto.getTasks(), EMPTY_LIST)));

        return dao;
    }

    public MockHandlerDto convert(final MockHandlerDao mockHandlerDao) {

        return MockHandlerDto.builder()
                .handlerId(Identity.from(mockHandlerDao.getHandlerId()))
                .name(mockHandlerDao.getName())
                .httpMethods(Arrays.stream(mockHandlerDao.getHttpMethods().split(",")).map(HttpMethod::resolve).collect(Collectors.toList()))
                .requestUri(mockHandlerDao.getRequestUri())
                .label(mockHandlerDao.getLabel())
                .delayTime(mockHandlerDao.getDelayTime())
                .customizeSpace(JSON.parseObject(mockHandlerDao.getCustomizeSpace()))
                .responses(JSON.parseArray(mockHandlerDao.getResponses(), MockResponseInfoDto.class))
                .tasks(JSON.parseArray(mockHandlerDao.getTasks(), TaskInfoDto.class))
                .build();
    }
}