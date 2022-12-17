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
 * @Date 2022/11/8 22:20
 * @Since
 **/
@Component
public class IsNotBlankFunctionFactory implements FunctionFactory {

    private static final FunctionInfo IS_NOT_BLANK_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("IsNotBlank")
            .minArgsNumber(0)
            .maxArgsNumber(1)
            .build();

    private static final String SYMBOL = "#isNotBlank(...)";

    private final IsBlankFunctionFactory isBlankFunctionFactory;

    public IsNotBlankFunctionFactory(final IsBlankFunctionFactory isBlankFunctionFactory) {
        this.isBlankFunctionFactory = isBlankFunctionFactory;
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return IS_NOT_BLANK_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "isNotBlank".equalsIgnoreCase(methodName);
    }

    @Override
    public IsNotBlankFunction buildFunction(final List<DynamicValue> dynamicValueList) {
        return IsNotBlankFunction.from(dynamicValueList, this.isBlankFunctionFactory);
    }

    /**
     * 判空函数
     * #isNotBlank(待检查字符)
     */
    public static class IsNotBlankFunction extends AbstractDynamicValue {

        private final IsBlankFunctionFactory.IsBlankFunction isBlankFunction;

        private IsNotBlankFunction(final List<DynamicValue> dynamicValueList, final IsBlankFunctionFactory.IsBlankFunction isBlankFunction) {
            super(dynamicValueList);
            this.isBlankFunction = isBlankFunction;
        }

        public static IsNotBlankFunction from(final List<DynamicValue> dynamicValueList, final IsBlankFunctionFactory isBlankFunctionFactory) {
            CheckUtils.sizeBetween(dynamicValueList, IS_NOT_BLANK_FUNCTION_INFO.getMinArgsNumber(), IS_NOT_BLANK_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of IsNotBlank function."));
            return new IsNotBlankFunction(dynamicValueList, isBlankFunctionFactory.buildFunction(dynamicValueList));
        }

        @Override
        public Boolean doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            return !this.isBlankFunction.doInterpret(mockHandlerContext, childNodeValueList);
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}