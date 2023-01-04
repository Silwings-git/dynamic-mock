package top.silwings.core.handler.tree.dynamic.function.functions;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.handler.tree.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.function.FunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.FunctionInfo;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.JsonUtils;

import java.util.List;

/**
 * @ClassName ToBeanFunctionFactory
 * @Description 文本转对象函数
 * @Author Silwings
 * @Date 2022/12/31 12:13
 * @Since
 **/
@Component
public class ToBeanFunctionFactory implements FunctionFactory {

    private static final FunctionInfo TO_BEAN_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("ToBean")
            .minArgsNumber(1)
            .maxArgsNumber(1)
            .build();

    private static final String SYMBOL = "#toBean(...)";

    @Override
    public boolean support(final String methodName) {
        return "toBean".equalsIgnoreCase(methodName);
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return TO_BEAN_FUNCTION_INFO;
    }

    @Override
    public ToBeanFunction buildFunction(final List<DynamicValue> dynamicValueList) {
        CheckUtils.sizeBetween(dynamicValueList, TO_BEAN_FUNCTION_INFO.getMinArgsNumber(), TO_BEAN_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of ToBean function."));
        return ToBeanFunction.form(dynamicValueList);
    }

    public static class ToBeanFunction extends AbstractDynamicValue {

        protected ToBeanFunction(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        public static ToBeanFunction form(final List<DynamicValue> dynamicValueList) {
            return new ToBeanFunction(dynamicValueList);
        }

        @Override
        protected Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            return JsonUtils.toBean(String.valueOf(childNodeValueList.get(0)));
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}