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
 * @ClassName IsEmptyFunctionFactory
 * @Description 集合判空函数工厂
 * @Author Silwings
 * @Date 2022/11/8 22:20
 * @Since
 **/
@Component
public class IsNotEmptyFunctionFactory implements FunctionFactory {

    private static final FunctionInfo IS_NOT_EMPTY_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("IsNotEmpty")
            .minArgsNumber(0)
            .maxArgsNumber(1)
            .build();

    private static final String SYMBOL = "#isNotEmpty(...)";

    private final IsEmptyFunctionFactory isEmptyFunctionFactory;

    public IsNotEmptyFunctionFactory(final IsEmptyFunctionFactory isEmptyFunctionFactory) {
        this.isEmptyFunctionFactory = isEmptyFunctionFactory;
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return IS_NOT_EMPTY_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "isNotEmpty".equalsIgnoreCase(methodName);
    }

    @Override
    public IsNotEmptyFunction buildFunction(final List<DynamicValue> dynamicValueList) {
        return IsNotEmptyFunction.from(dynamicValueList, this.isEmptyFunctionFactory);
    }

    /**
     * 集合判空函数
     * #isNotEmpty(待检查集合)
     * 如果参数不为空且不是集合，映射，或可转换为集合，映射的字符串类型，抛出异常
     */
    public static class IsNotEmptyFunction extends AbstractDynamicValue {

        private final IsEmptyFunctionFactory.IsEmptyFunction isEmptyFunction;

        private IsNotEmptyFunction(final List<DynamicValue> dynamicValueList, final IsEmptyFunctionFactory.IsEmptyFunction isEmptyFunction) {
            super(dynamicValueList);
            this.isEmptyFunction = isEmptyFunction;
        }

        public static IsNotEmptyFunction from(final List<DynamicValue> dynamicValueList, final IsEmptyFunctionFactory isEmptyFunctionFactory) {
            CheckUtils.sizeBetween(dynamicValueList, IS_NOT_EMPTY_FUNCTION_INFO.getMinArgsNumber(), IS_NOT_EMPTY_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of IsNotEmpty function."));
            return new IsNotEmptyFunction(dynamicValueList, isEmptyFunctionFactory.buildFunction(dynamicValueList));
        }

        @Override
        public Boolean doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            return !this.isEmptyFunction.doInterpret(mockHandlerContext, childNodeValueList);
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}