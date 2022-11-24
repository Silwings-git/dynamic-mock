package top.silwings.core.handler.tree.dynamic.function.functions;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.handler.tree.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.function.FunctionFactory;
import top.silwings.core.utils.CheckUtils;

import java.util.List;

/**
 * @ClassName EqualsFunctionFactory
 * @Description 相等函数工厂
 * @Author Silwings
 * @Date 2022/11/8 22:20
 * @Since
 **/
@Component
public class EqualsFunctionFactory implements FunctionFactory {

    private static final String SYMBOL = "#equals(...)";

    @Override
    public boolean support(final String methodName) {
        return "eq".equalsIgnoreCase(methodName) || "equals".equalsIgnoreCase(methodName);
    }

    @Override
    public DynamicValue buildFunction(final List<DynamicValue> dynamicValueList) {
        return EqualsFunction.from(dynamicValueList);
    }

    /**
     * 相等函数
     * #equal(待检查字符(大于等于2个))
     * #eq(待检查字符(大于等于2个))
     */
    public static class EqualsFunction extends AbstractDynamicValue {

        private EqualsFunction(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        public static EqualsFunction from(final List<DynamicValue> dynamicValueList) {
            CheckUtils.hasMinimumSize(dynamicValueList, 2, () -> DynamicValueCompileException.from("The Equals function requires at least 2 arguments."));
            return new EqualsFunction(dynamicValueList);
        }

        @Override
        public Boolean doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            if (childNodeValueList.isEmpty() || childNodeValueList.size() < this.getNodeCount() || childNodeValueList.size() < 2) {
                throw new DynamicMockException("Parameter incorrectly of `equals` function. expect: " + (this.getNodeCount() > 2 ? this.getNodeCount() : 2) + ", actual: " + childNodeValueList.size());
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

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}