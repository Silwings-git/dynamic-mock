package top.silwings.core.handler.tree.dynamic.function.functions;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.handler.tree.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.function.FunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.FunctionInfo;
import top.silwings.core.utils.CheckUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName JoinFunctionFactory
 * @Description 字符JOIN
 * @Author Silwings
 * @Date 2022/12/2 0:10
 * @Since
 **/
@Component
public class JoinFunctionFactory implements FunctionFactory {

    private static final FunctionInfo JOIN_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("Join")
            .minArgsNumber(2)
            .maxArgsNumber(Integer.MAX_VALUE)
            .build();

    private static final String SYMBOL = "#join(...)";

    @Override
    public FunctionInfo getFunctionInfo() {
        return JOIN_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "join".equalsIgnoreCase(methodName);
    }

    @Override
    public JoinFunction buildFunction(final List<DynamicValue> dynamicValueList) {
        CheckUtils.sizeBetween(dynamicValueList, JOIN_FUNCTION_INFO.getMinArgsNumber(), JOIN_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of Join function."));
        return JoinFunction.from(dynamicValueList);
    }

    /**
     * 字符串Join函数
     * #join(连接符,待连接字符)
     */
    public static class JoinFunction extends AbstractDynamicValue {

        private JoinFunction(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        private static JoinFunctionFactory.JoinFunction from(final List<DynamicValue> dynamicValueList) {
            return new JoinFunctionFactory.JoinFunction(dynamicValueList);
        }

        @Override
        public String doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            final Object first = childNodeValueList.get(0);

            final List<String> strList = new ArrayList<>(childNodeValueList.size() - 1);

            for (int i = 1; i < childNodeValueList.size(); i++) {
                final Object value = childNodeValueList.get(i);
                if (value instanceof List) {
                    strList.addAll(((List<?>) value).stream().map(e -> null == e ? null : String.valueOf(e)).collect(Collectors.toList()));
                } else {
                    strList.add(null == value ? null : String.valueOf(value));
                }
            }

            return String.join(String.valueOf(first), strList);
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}