package top.silwings.core.handler.tree.dynamic.function.functions;

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
 * @Date 2022/12/14 23:41
 * @Since
 **/
@Component
public class IsNotNullFunctionFactory implements FunctionFactory {

    private static final FunctionInfo IS_NOT_NULL_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("IsNotNull")
            .minArgsNumber(0)
            .maxArgsNumber(1)
            .build();

    private static final String SYMBOL = "#isNotNull(...)";

    private final IsNullFunctionFactory isNullFunctionFactory;

    public IsNotNullFunctionFactory(final IsNullFunctionFactory isNullFunctionFactory) {
        this.isNullFunctionFactory = isNullFunctionFactory;
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return IS_NOT_NULL_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "isnotnull".equalsIgnoreCase(methodName);
    }

    @Override
    public IsNotNullFunction buildFunction(final List<DynamicValue> dynamicValueList) {
        CheckUtils.sizeBetween(dynamicValueList, IS_NOT_NULL_FUNCTION_INFO.getMinArgsNumber(), IS_NOT_NULL_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of IsNotNull function."));
        return IsNotNullFunction.from(dynamicValueList, this.isNullFunctionFactory.buildFunction(dynamicValueList));
    }

    /**
     * 判空函数
     * #isNotNull(待检查实例)
     */
    public static class IsNotNullFunction extends AbstractDynamicValue {

        private final IsNullFunctionFactory.IsNullFunction isNullFunction;

        private IsNotNullFunction(final List<DynamicValue> dynamicValueList, final IsNullFunctionFactory.IsNullFunction isNullFunction) {
            super(dynamicValueList);
            this.isNullFunction = isNullFunction;
        }

        private static IsNotNullFunction from(final List<DynamicValue> dynamicValueList, final IsNullFunctionFactory.IsNullFunction isNullFunction) {
            return new IsNotNullFunction(dynamicValueList, isNullFunction);
        }

        @Override
        public Boolean doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            return !this.isNullFunction.doInterpret(mockHandlerContext, childNodeValueList);
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}