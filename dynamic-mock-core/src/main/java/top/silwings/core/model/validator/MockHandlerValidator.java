package top.silwings.core.model.validator;

import org.springframework.stereotype.Component;
import top.silwings.core.handler.MockHandlerFactory;
import top.silwings.core.model.MockHandlerDto;

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

    public MockHandlerValidator(final MockHandlerFactory mockHandlerFactory) {
        this.mockHandlerFactory = mockHandlerFactory;
    }

    public void validate(final MockHandlerDto mockHandlerDto) {
        this.mockHandlerFactory.buildMockHandler(mockHandlerDto);
    }

}