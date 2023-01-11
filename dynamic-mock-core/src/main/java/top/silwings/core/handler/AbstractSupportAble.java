package top.silwings.core.handler;

import org.apache.commons.collections4.CollectionUtils;
import top.silwings.core.interpreter.ExpressionInterpreter;

import java.util.List;

/**
 * @ClassName AbstractMockSupport
 * @Description
 * @Author Silwings
 * @Date 2022/11/12 22:08
 * @Since
 **/
public abstract class AbstractSupportAble {

    protected abstract List<ExpressionInterpreter> getSupportInterpreterList();

    public boolean support(final MockHandlerContext mockHandlerContext) {

        if (CollectionUtils.isEmpty(this.getSupportInterpreterList())) {
            return true;
        }

        for (final ExpressionInterpreter interpreter : this.getSupportInterpreterList()) {
            if (!Boolean.TRUE.equals(interpreter.interpret(mockHandlerContext))) {
                return false;
            }
        }

        return true;
    }

}