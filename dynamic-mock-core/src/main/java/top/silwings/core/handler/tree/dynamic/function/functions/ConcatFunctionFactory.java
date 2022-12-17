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
 * @ClassName ConcatFunctionFactory
 * @Description 字符JOIN
 * @Author Silwings
 * @Date 2022/12/2 0:10
 * @Since
 **/
@Component
public class ConcatFunctionFactory implements FunctionFactory {

    private static final FunctionInfo CONCAT_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("Concat")
            .minArgsNumber(0)
            .maxArgsNumber(Integer.MAX_VALUE)
            .build();

    private static final String SYMBOL = "#concat(...)";

    @Override
    public FunctionInfo getFunctionInfo() {
        return CONCAT_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "concat".equalsIgnoreCase(methodName);
    }

    @Override
    public ConcatFunction buildFunction(final List<DynamicValue> dynamicValueList) {
        return ConcatFunctionFactory.ConcatFunction.from(dynamicValueList);
    }

    /**
     * 字符串连接函数
     * #concat(待连接字符,待连接字符...)
     */
    public static class ConcatFunction extends AbstractDynamicValue {

        private ConcatFunction(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        public static ConcatFunctionFactory.ConcatFunction from(final List<DynamicValue> dynamicValueList) {
            CheckUtils.sizeBetween(dynamicValueList, CONCAT_FUNCTION_INFO.getMinArgsNumber(), CONCAT_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of Concat function."));
            return new ConcatFunctionFactory.ConcatFunction(dynamicValueList);
        }

        @Override
        public String doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            final StringBuilder builder = new StringBuilder();

            childNodeValueList.stream().map(e -> null == e ? null : String.valueOf(e)).forEach(builder::append);

            return builder.toString();
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}