package top.silwings.admin.repository.converter;

import org.springframework.stereotype.Component;
import top.silwings.admin.repository.po.MockTaskLogPo;
import top.silwings.core.common.Identity;
import top.silwings.core.model.MockTaskLogDto;
import top.silwings.core.utils.ConvertUtils;

/**
 * @ClassName MockHandlerDaoConverter
 * @Description
 * @Author Silwings
 * @Date 2022/11/15 23:00
 * @Since
 **/
@Component
public class MockTaskLogDaoConverter {

    private static final String EMPTY_JSON = "{}";

    public MockTaskLogPo convert(final MockTaskLogDto mockTaskLog) {

        final MockTaskLogPo logPo = new MockTaskLogPo();
        logPo.setLogId(ConvertUtils.getNoNullOrDefault(mockTaskLog.getLogId(), null, Identity::intValue));
        logPo.setTaskCode(mockTaskLog.getTaskCode());
        logPo.setHandlerId(ConvertUtils.getNoNullOrDefault(mockTaskLog.getHandlerId(), null, Identity::intValue));
        logPo.setName(mockTaskLog.getName());
        logPo.setRegistrationTime(mockTaskLog.getRegistrationTime());
        logPo.setRequestInfo(ConvertUtils.getNoBlankOrDefault(mockTaskLog.getRequestInfo(), EMPTY_JSON));
        logPo.setResponseInfo(ConvertUtils.getNoBlankOrDefault(mockTaskLog.getResponseInfo(), EMPTY_JSON));
        logPo.setRequestTime(mockTaskLog.getRequestTime());
        logPo.setTiming(mockTaskLog.getTiming());
        return logPo;
    }

    public MockTaskLogDto convert(final MockTaskLogPo mockTaskLog) {

        return MockTaskLogDto.builder()
                .logId(Identity.from(mockTaskLog.getLogId()))
                .taskCode(mockTaskLog.getTaskCode())
                .handlerId(Identity.from(mockTaskLog.getHandlerId()))
                .name(mockTaskLog.getName())
                .registrationTime(mockTaskLog.getRegistrationTime())
                .requestInfo(mockTaskLog.getRequestInfo())
                .responseInfo(mockTaskLog.getResponseInfo())
                .requestTime(mockTaskLog.getRequestTime())
                .timing(mockTaskLog.getTiming())
                .build();
    }


}