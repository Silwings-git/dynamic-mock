package cn.silwings.core.handler.check;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import cn.silwings.core.handler.context.MockHandlerContext;
import cn.silwings.core.handler.response.CommonMockResponse;
import cn.silwings.core.handler.response.MockResponse;
import cn.silwings.core.utils.JsonUtils;
import cn.silwings.core.utils.MessageFormatUtils;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ParameterCheck
 * @Description 验证
 * @Author Silwings
 * @Date 2023/8/11 10:13
 * @Since
 **/
@Getter
@Builder
public class CheckInfo {

    public static final MockResponse DEFAULT_ERROR_RES = ErrorResponseInfo.builder().status(400).body("Parameter check failed").build();

    /**
     * 错误信息
     */
    private final Map<String, ErrorResponseInfo> errResMap;

    /**
     * 校验项
     */
    private final List<CheckItem> checkItemList;

    public CheckResult check(final MockHandlerContext mockHandlerContext) {

        if (CollectionUtils.isEmpty(this.checkItemList)) {
            return CheckResult.ok();
        }

        for (final CheckItem checkItem : this.checkItemList) {
            final boolean passed = checkItem.check(mockHandlerContext);
            if (!passed) {
                return CheckResult.fail(this.fillErrorMsg(checkItem));
            }
        }

        return CheckResult.ok();
    }

    private MockResponse fillErrorMsg(final CheckItem checkItem) {

        final String errResCode = checkItem.getErrResCode();

        final ErrorResponseInfo errRes = this.errResMap.get(errResCode);

        if (null == errRes) {
            return DEFAULT_ERROR_RES;
        }

        if (CollectionUtils.isNotEmpty(checkItem.getErrMsgFillParam())) {
            final Object[] arguments = checkItem.getErrMsgFillParam().toArray();
            return CommonMockResponse.builder()
                    .delayTime(0)
                    .status(errRes.getStatus())
                    .headers(errRes.getHeaders())
                    .body(JsonUtils.tryToBean(this.fillParam(errRes.getBody(), arguments)))
                    .build();
        }

        return errRes;
    }

    private String fillParam(final Object pattern, Object[] arguments) {

        if (null == pattern) {
            return null;
        }

        if (pattern instanceof String) {
            return MessageFormat.format((String) pattern, arguments);
        } else {
            final String jsonString = JsonUtils.toJSONString(pattern);
            // 这里因为会有Json数据,MessageFormat不能很好的工作,所以使用自定义的MessageFormatUtils
            return MessageFormatUtils.format(jsonString, arguments);
        }
    }

}