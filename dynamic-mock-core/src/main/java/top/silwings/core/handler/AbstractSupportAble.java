package top.silwings.core.handler;

import org.apache.commons.collections4.CollectionUtils;
import top.silwings.core.handler.tree.NodeInterpreter;

import java.util.List;

/**
 * @ClassName AbstractMockSupport
 * @Description
 * @Author Silwings
 * @Date 2022/11/12 22:08
 * @Since
 **/
public abstract class AbstractSupportAble {

    protected abstract List<NodeInterpreter> getSupportInterpreterList();

    public boolean support(final MockHandlerContext mockHandlerContext) {

        if (CollectionUtils.isEmpty(this.getSupportInterpreterList())) {
            return true;
        }

        for (final NodeInterpreter interpreter : this.getSupportInterpreterList()) {
            if (!Boolean.TRUE.equals(interpreter.interpret(mockHandlerContext))) {
                return false;
            }
        }

        return true;
    }

}