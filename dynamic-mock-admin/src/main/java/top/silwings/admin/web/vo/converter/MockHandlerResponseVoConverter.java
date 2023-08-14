package top.silwings.admin.web.vo.converter;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import top.silwings.admin.web.vo.param.MockResponseInfoParam;
import top.silwings.admin.web.vo.param.MockResponseParam;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.model.MockResponseDto;
import top.silwings.core.model.MockResponseInfoDto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<MockResponseInfoDto> convert(final List<MockResponseInfoParam> voList) {
        if (CollectionUtils.isEmpty(voList)) {
            return Collections.emptyList();
        }
        return voList.stream().map(this::convert).collect(Collectors.toList());
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

    public MockResponseInfoParam convert(final MockResponseInfoDto dto) {
        return MockResponseInfoParam.builder()
                .responseId(dto.getResponseId())
                .name(dto.getName())
                .enableStatus(dto.getEnableStatus().code())
                .support(dto.getSupport())
                .delayTime(dto.getDelayTime())
                .checkInfo(this.mockHandlerCheckVoConverter.convert(dto.getCheckInfo()))
                .response(this.convert(dto.getResponse()))
                .build();
    }

    private MockResponseParam convert(final MockResponseDto dto) {
        return MockResponseParam.builder()
                .status(dto.getStatus())
                .headers(dto.getHeaders())
                .body(dto.getBody())
                .build();
    }

}