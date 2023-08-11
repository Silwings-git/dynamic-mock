package top.silwings.admin.web.vo.converter;

import org.springframework.stereotype.Component;
import top.silwings.admin.web.vo.param.MockResponseInfoParam;
import top.silwings.admin.web.vo.param.MockResponseParam;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.model.MockResponseDto;
import top.silwings.core.model.MockResponseInfoDto;

/**
 * @ClassName MockHandlerResponseVoConverter
 * @Description
 * @Author Silwings
 * @Date 2023/8/8 17:26
 * @Since
 **/
@Component
public class MockHandlerResponseVoConverter {

    private final MockHandlerCheckVoConverter mockHandlerCheckVoConverter;

    public MockHandlerResponseVoConverter(final MockHandlerCheckVoConverter mockHandlerCheckVoConverter) {
        this.mockHandlerCheckVoConverter = mockHandlerCheckVoConverter;
    }

    public MockResponseInfoDto convert(final MockResponseInfoParam vo) {
        return MockResponseInfoDto.builder()
                .responseId(vo.getResponseId())
                .name(vo.getName())
                .enableStatus(EnableStatus.valueOfCode(vo.getEnableStatus()))
                .support(vo.getSupport())
                .delayTime(vo.getDelayTime())
                .checkInfo(this.mockHandlerCheckVoConverter.convert(vo.getCheckInfo()))
                .response(this.convert(vo.getResponse()))
                .build();
    }

    private MockResponseDto convert(final MockResponseParam vo) {
        return MockResponseDto.builder()
                .status(vo.getStatus())
                .headers(vo.getHeaders())
                .body(vo.getBody())
                .build();
    }

}