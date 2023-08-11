package top.silwings.core.handler.check;

import lombok.Builder;
import lombok.Getter;
import top.silwings.core.handler.response.MockResponse;

/**
 * @ClassName CheckResult
 * @Description 检查结果
 * @Author Silwings
 * @Date 2023/8/11 10:53
 * @Since
 **/
@Getter
@Builder
public class CheckResult {

    /**
     * 是否通过
     */
    private final boolean passed;

    /**
     * 检查结果产生的响应信息,如果passed=true,则该字段一定为null
     */
    private final MockResponse checkFailedResponse;

    public static CheckResult ok() {
        return CheckResult.builder().passed(true).build();
    }

    public static CheckResult fail(final MockResponse checkFailedResponse) {
        return CheckResult.builder().passed(false).checkFailedResponse(checkFailedResponse).build();
    }

}