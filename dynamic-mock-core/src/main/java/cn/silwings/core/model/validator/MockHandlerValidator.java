package cn.silwings.core.model.validator;

import org.springframework.stereotype.Component;
import cn.silwings.core.handler.MockHandlerFactory;
import cn.silwings.core.handler.response.MockResponseInfoFactory;
import cn.silwings.core.model.MockHandlerDto;
import cn.silwings.core.model.MockResponseInfoDto;

/**
 * @ClassName MockHandlerValidator
 * @Description
 * @Author Silwings
 * @Date 2022/11/20 12:03
 * @Since
 **/
@Component
public class MockHandlerValidator {

    private final MockHandlerFactory mockHandlerFactory;
    private final MockResponseInfoFactory mockResponseInfoFactory;

    public MockHandlerValidator(final MockHandlerFactory mockHandlerFactory, final MockResponseInfoFactory mockResponseInfoFactory) {
        this.mockHandlerFactory = mockHandlerFactory;
        this.mockResponseInfoFactory = mockResponseInfoFactory;
    }

    public void validate(final MockHandlerDto mockHandlerDto) {
        this.mockHandlerFactory.buildMockHandler(mockHandlerDto);
    }

    public void validateMockResponse(final MockResponseInfoDto responseInfoDto) {
        this.mockResponseInfoFactory.buildResponseInfo(responseInfoDto);
    }
}