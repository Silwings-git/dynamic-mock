package top.silwings.core.handler.tree.dynamic.function.functions;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
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
public class IsBlankFunctionFactory implements FunctionFactory {

    private static final String SYMBOL = "#isBlank(...)";

    @Override
    public boolean support(final String methodName) {
        return "isBlank".equalsIgnoreCase(methodName);
    }

    @Override
    public DynamicValue buildFunction(final List<DynamicValue> dynamicValueList) {
        return IsBlankFunction.from(dynamicValueList);
    }

    /**
     * 判空函数
     * #isBlank(待检查字符)
     */
    public static class IsBlankFunction extends AbstractDynamicValue {

        public IsBlankFunction(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        public static IsBlankFunction from(final List<DynamicValue> dynamicValueList) {
            return new IsBlankFunction(dynamicValueList);
        }

        @Override
        public Object doInterpret(final Context context, final List<Object> childNodeValueList) {
            return CollectionUtils.isEmpty(childNodeValueList)
                    || null == childNodeValueList.get(0)
                    || (childNodeValueList.get(0) instanceof String
                    && StringUtils.isBlank((CharSequence) childNodeValueList.get(0)));
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}