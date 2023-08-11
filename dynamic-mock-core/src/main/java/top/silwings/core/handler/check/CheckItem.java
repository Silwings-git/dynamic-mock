package top.silwings.core.handler.check;

import lombok.Builder;
import lombok.Getter;
import top.silwings.core.handler.context.MockHandlerContext;
import top.silwings.core.interpreter.ExpressionInterpreter;

import java.util.List;

/**
 * @ClassName CheckItem
 * @Description
 * @Author Silwings
 * @Date 2023/8/11 10:20
 * @Since
 **/
@Getter
@Builder
public class CheckItem {

    /**
     * 错误消息Id
     */
    private final String errResCode;

    /**
     * 错误消息填充参数
     */
    private final List<String> errMsgFillParam;

    /**
     * 检查语句
     */
    private final ExpressionInterpreter checkInterpreter;

    public boolean check(final MockHandlerContext mockHandlerContext) {
        return Boolean.TRUE.equals(this.checkInterpreter.interpret(mockHandlerContext));
    }
}