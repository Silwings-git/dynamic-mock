package top.silwings.core.handler.tree.dynamic.function.functions;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.handler.tree.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.function.FunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.FunctionInfo;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.TypeUtils;

import java.util.List;

/**
 * @ClassName SelectFunctionFactory
 * @Description 选择函数(三元运算符)
 * @Author Silwings
 * @Date 2023/1/8 22:21
 * @Since
 **/
@Component
public class SelectFunctionFactory implements FunctionFactory {

    private static final FunctionInfo SELECT_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("Concat")
            .minArgsNumber(3)
            .maxArgsNumber(3)
            .build();

    private static final String SYMBOL = "#select(...)";

    @Override
    public boolean support(final String methodName) {
        return "select".equalsIgnoreCase(methodName);
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return SELECT_FUNCTION_INFO;
    }

    @Override
    public DynamicValue buildFunction(final List<DynamicValue> dynamicValueList) {
        CheckUtils.sizeBetween(dynamicValueList, SELECT_FUNCTION_INFO.getMinArgsNumber(), SELECT_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of Select function."));
        return SelectFunction.from(dynamicValueList);
    }

    /**
     * 选择函数
     * 等于三元运算表达式
     * #select(boolean,argA,argB)
     */
    public static class SelectFunction extends AbstractDynamicValue {

        public SelectFunction(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        public static SelectFunction from(final List<DynamicValue> dynamicValueList) {
            return new SelectFunction(dynamicValueList);
        }

        @Override
        protected Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            return Boolean.TRUE.equals(TypeUtils.toBooleanValue(childNodeValueList.get(0))) ? childNodeValueList.get(1) : childNodeValueList.get(2);
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }

    }

}