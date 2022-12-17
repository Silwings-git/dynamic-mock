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
 * @ClassName EqualsFunctionFactory
 * @Description 不等函数工厂
 * @Author Silwings
 * @Date 2022/12/17 18:24
 * @Since
 **/
@Component
public class NoEqualsFunctionFactory implements FunctionFactory {

    private static final FunctionInfo NO_EQUALS_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("NoEquals")
            .minArgsNumber(2)
            .maxArgsNumber(2)
            .build();

    private static final String SYMBOL = "#noEquals(...)";

    private final EqualsFunctionFactory equalsFunctionFactory;

    public NoEqualsFunctionFactory(final EqualsFunctionFactory equalsFunctionFactory) {
        this.equalsFunctionFactory = equalsFunctionFactory;
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return NO_EQUALS_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "neq".equalsIgnoreCase(methodName) || "noequals".equalsIgnoreCase(methodName);
    }

    @Override
    public NoEqualsFunction buildFunction(final List<DynamicValue> dynamicValueList) {
        CheckUtils.sizeBetween(dynamicValueList, NO_EQUALS_FUNCTION_INFO.getMinArgsNumber(), NO_EQUALS_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of NoEquals function."));
        return NoEqualsFunction.from(dynamicValueList, this.equalsFunctionFactory.buildFunction(dynamicValueList));
    }

    /**
     * 相等函数
     * #noEqual(待检查字符A,待检查字符B)
     * #neq(待检查字符A,待检查字符B)
     */
    public static class NoEqualsFunction extends AbstractDynamicValue {

        private final EqualsFunctionFactory.EqualsFunction equalsFunction;

        private NoEqualsFunction(final List<DynamicValue> dynamicValueList, final EqualsFunctionFactory.EqualsFunction equalsFunction) {
            super(dynamicValueList);
            this.equalsFunction = equalsFunction;
        }

        public static NoEqualsFunction from(final List<DynamicValue> dynamicValueList, final EqualsFunctionFactory.EqualsFunction equalsFunction) {
            return new NoEqualsFunction(dynamicValueList, equalsFunction);
        }

        @Override
        public Boolean doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            return !this.equalsFunction.doInterpret(mockHandlerContext, childNodeValueList);
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}