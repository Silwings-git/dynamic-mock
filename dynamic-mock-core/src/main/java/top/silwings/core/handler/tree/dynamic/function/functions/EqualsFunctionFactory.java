package top.silwings.core.handler.tree.dynamic.function.functions;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicDataException;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.tree.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.function.FunctionFactory;

import java.util.List;

/**
 * @ClassName SearchFunctionFactory
 * @Description 搜索函数工厂
 * @Author Silwings
 * @Date 2022/11/8 22:20
 * @Since
 **/
@Component
public class EqualsFunctionFactory implements FunctionFactory {

    @Override
    public boolean support(final String methodName) {
        return "eq".equalsIgnoreCase(methodName) || "equals".equalsIgnoreCase(methodName);
    }

    @Override
    public DynamicValue buildFunction(final List<DynamicValue> dynamicValueList) {
        return EqualsFunction.from(dynamicValueList);
    }

    /**
     * 判空函数
     * #isBlank(待检查字符)
     */
    public static class EqualsFunction extends AbstractDynamicValue {

        public EqualsFunction(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        public static EqualsFunction from(final List<DynamicValue> dynamicValueList) {
            return new EqualsFunction(dynamicValueList);
        }

        @Override
        public Boolean interpret(final Context context, final List<Object> childNodeValueList) {

            if (childNodeValueList.isEmpty() || childNodeValueList.size() < this.getNodeCount() || childNodeValueList.size() < 2) {
                throw new DynamicDataException("缺少参数.");
            }

            final String first = String.valueOf(childNodeValueList.get(0));
            if (null == first) {
                return false;
            }

            for (final Object obj : childNodeValueList) {
                if (null == obj || !String.valueOf(obj).equals(first)) {
                    return false;
                }
            }

            return true;
        }

    }

}