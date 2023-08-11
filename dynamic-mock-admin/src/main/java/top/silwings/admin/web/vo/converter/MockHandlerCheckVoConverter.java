package top.silwings.admin.web.vo.converter;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import top.silwings.admin.web.vo.param.CheckInfoParam;
import top.silwings.admin.web.vo.param.CheckItemParam;
import top.silwings.admin.web.vo.param.ErrorResponseInfoParam;
import top.silwings.core.model.CheckInfoDto;
import top.silwings.core.model.CheckItemDto;
import top.silwings.core.model.ErrorResponseInfoDto;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerCheckVoConverter
 * @Description
 * @Author Silwings
 * @Date 2023/8/11 18:26
 * @Since
 **/
@Component
public class MockHandlerCheckVoConverter {

    public CheckInfoDto convert(final CheckInfoParam checkInfo) {
        final CheckInfoDto infoParam = new CheckInfoDto();
        infoParam.setErrResList(CollectionUtils.isNotEmpty(checkInfo.getErrResList())
                ? checkInfo.getErrResList().stream().map(this::convert).collect(Collectors.toList())
                : Collections.emptyList());
        infoParam.setCheckItemList(CollectionUtils.isNotEmpty(checkInfo.getCheckItemList())
                ? checkInfo.getCheckItemList().stream().map(this::convert).collect(Collectors.toList())
                : Collections.emptyList());
        return infoParam;
    }

    public CheckItemDto convert(final CheckItemParam checkItemDto) {
        return new CheckItemDto()
                .setErrResCode(checkItemDto.getErrResCode())
                .setErrResCode(checkItemDto.getErrResCode())
                .setErrMsgFillParam(checkItemDto.getErrMsgFillParam())
                .setCheckExpression(checkItemDto.getCheckExpression());
    }

    public ErrorResponseInfoDto convert(final ErrorResponseInfoParam responseInfoDto) {
        return new ErrorResponseInfoDto()
                .setErrResCode(responseInfoDto.getErrResCode())
                .setStatus(responseInfoDto.getStatus())
                .setHeaders(responseInfoDto.getHeaders())
                .setBody(responseInfoDto.getBody());
    }

    public CheckInfoParam convert(final CheckInfoDto checkInfo) {
        final CheckInfoParam infoParam = new CheckInfoParam();
        infoParam.setErrResList(CollectionUtils.isNotEmpty(checkInfo.getErrResList())
                ? checkInfo.getErrResList().stream().map(this::convert).collect(Collectors.toList())
                : Collections.emptyList());
        infoParam.setCheckItemList(CollectionUtils.isNotEmpty(checkInfo.getCheckItemList())
                ? checkInfo.getCheckItemList().stream().map(this::convert).collect(Collectors.toList())
                : Collections.emptyList());
        return infoParam;
    }

    public CheckItemParam convert(final CheckItemDto checkItemDto) {
        return new CheckItemParam()
                .setErrResCode(checkItemDto.getErrResCode())
                .setErrResCode(checkItemDto.getErrResCode())
                .setErrMsgFillParam(checkItemDto.getErrMsgFillParam())
                .setCheckExpression(checkItemDto.getCheckExpression());
    }

    public ErrorResponseInfoParam convert(final ErrorResponseInfoDto responseInfoDto) {
        return new ErrorResponseInfoParam()
                .setErrResCode(responseInfoDto.getErrResCode())
                .setStatus(responseInfoDto.getStatus())
                .setHeaders(responseInfoDto.getHeaders())
                .setBody(responseInfoDto.getBody());
    }

}