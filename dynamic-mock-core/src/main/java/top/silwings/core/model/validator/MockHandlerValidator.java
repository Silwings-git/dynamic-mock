package top.silwings.core.model.validator;

import org.springframework.stereotype.Component;
import top.silwings.core.handler.MockHandlerFactory;
import top.silwings.core.handler.response.MockResponseInfoFactory;
import top.silwings.core.model.MockHandlerDto;
import top.silwings.core.model.MockResponseInfoDto;

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