package top.silwings.core.handler.dynamic.function;

import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.DynamicValue;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName FunctionDynamicValue
 * @Description 函数
 * @Author Silwings
 * @Date 2022/11/7 20:52
 * @Since
 **/
public abstract class AbstractDynamicValue implements DynamicValue {

    private final DynamicValue param;

    protected AbstractDynamicValue(final DynamicValue param) {
        this.param = param;
    }

    /**
     * 提供最终值的参数信息
     *
     * @param parameterContext 参数上下文
     * @return 最终值数值
     */
    protected List<Object> getParams(final Context parameterContext) {

        final Object value = this.param.interpret(parameterContext);
        if (null == value) {
            return Collections.emptyList();
        } else if (value instanceof List<?>) {
            return (List<Object>) value;
        } else {
            return Collections.singletonList(value);
        }
    }

}