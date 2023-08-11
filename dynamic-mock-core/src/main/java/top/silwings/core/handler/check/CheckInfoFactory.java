package top.silwings.core.handler.check;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.interpreter.ExpressionInterpreter;
import top.silwings.core.interpreter.dynamic_expression.DynamicExpressionFactory;
import top.silwings.core.model.CheckInfoDto;
import top.silwings.core.model.CheckItemDto;
import top.silwings.core.model.ErrorResponseInfoDto;
import top.silwings.core.utils.HttpUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName CheckInfoFactory
 * @Description
 * @Author Silwings
 * @Date 2023/8/11 15:00
 * @Since
 **/
@Component
public class CheckInfoFactory {

    private final DynamicExpressionFactory dynamicExpressionFactory;

    public CheckInfoFactory(final DynamicExpressionFactory dynamicExpressionFactory) {
        this.dynamicExpressionFactory = dynamicExpressionFactory;
    }

    public CheckInfo buildCheck(final CheckInfoDto checkInfo) {

        if (null == checkInfo) {
            return CheckInfo.builder().build();
        }

        return CheckInfo.builder()
                .errResMap(this.buildResMap(checkInfo.getErrResList()))
                .checkItemList(this.buildCheckItems(checkInfo.getCheckItemList()))
                .build();
    }

    private List<CheckItem> buildCheckItems(final List<CheckItemDto> checkItemList) {
        if (CollectionUtils.isEmpty(checkItemList)) {
            return Collections.emptyList();
        }
        return checkItemList.stream().map(this::buildCheckItem).collect(Collectors.toList());
    }

    private CheckItem buildCheckItem(final CheckItemDto checkItemDto) {
        return CheckItem.builder()
                .errResCode(checkItemDto.getErrResCode())
                .errMsgFillParam(checkItemDto.getErrMsgFillParam())
                .checkInterpreter(new ExpressionInterpreter(this.dynamicExpressionFactory.buildDynamicValue(checkItemDto.getCheckExpression())))
                .build();
    }

    private Map<String, ErrorResponseInfo> buildResMap(final List<ErrorResponseInfoDto> errResList) {

        if (CollectionUtils.isEmpty(errResList)) {
            return Collections.emptyMap();
        }

        return errResList.stream().collect(Collectors.toMap(ErrorResponseInfoDto::getErrResCode, this::buildErrorResponseInfo));
    }

    private ErrorResponseInfo buildErrorResponseInfo(final ErrorResponseInfoDto responseInfoDto) {
        return ErrorResponseInfo.builder()
                .status(responseInfoDto.getStatus())
                .headers(HttpUtils.convertMapToHttpHeaders(responseInfoDto.getHeaders()))
                .body(responseInfoDto.getBody())
                .build();
    }

}