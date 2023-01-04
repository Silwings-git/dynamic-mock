package top.silwings.core.handler.tree.dynamic.function.functions;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.handler.tree.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.function.FunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.FunctionInfo;
import top.silwings.core.utils.CheckUtils;

import java.util.List;

/**
 * @ClassName IsBlankFunctionFactory
 * @Description 判空函数工厂
 * @Author Silwings
 * @Date 2022/11/8 22:20
 * @Since
 **/
@Component
public class IsBlankFunctionFactory implements FunctionFactory {

    private static final FunctionInfo IS_BLANK_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("IsBlank")
            .minArgsNumber(0)
            .maxArgsNumber(1)
            .build();

    private static final String SYMBOL = "#isBlank(...)";

    @Override
    public FunctionInfo getFunctionInfo() {
        return IS_BLANK_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "isBlank".equalsIgnoreCase(methodName);
    }

    @Override
    public IsBlankFunction buildFunction(final List<DynamicValue> dynamicValueList) {
        CheckUtils.sizeBetween(dynamicValueList, IS_BLANK_FUNCTION_INFO.getMinArgsNumber(), IS_BLANK_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of IsBlank function."));
        return IsBlankFunction.from(dynamicValueList);
    }

    /**
     * 判空函数
     * #isBlank(待检查字符)
     */
    public static class IsBlankFunction extends AbstractDynamicValue {

        private IsBlankFunction(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        private static IsBlankFunction from(final List<DynamicValue> dynamicValueList) {
            return new IsBlankFunction(dynamicValueList);
        }

        @Override
        public Boolean doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            if (CollectionUtils.isEmpty(childNodeValueList)) {
                return true;
            }

            final Object ele = childNodeValueList.get(0);

            if (null == ele) {
                return true;
            }

            return StringUtils.isBlank(String.valueOf(ele));
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}