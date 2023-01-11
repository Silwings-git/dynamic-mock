package top.silwings.core.interpreter.expression.function.function_factory;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.interpreter.Expression;
import top.silwings.core.interpreter.expression.function.AbstractFunctionExpression;
import top.silwings.core.interpreter.expression.function.FunctionFactory;
import top.silwings.core.interpreter.expression.function.FunctionInfo;
import top.silwings.core.interpreter.expression.function.FunctionReturnType;
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
            .functionReturnType(FunctionReturnType.STRING)
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
    public JoinFunction buildFunction(final List<Expression> functionExpressionList) {
        CheckUtils.sizeBetween(functionExpressionList, JOIN_FUNCTION_INFO.getMinArgsNumber(), JOIN_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of Join function."));
        return JoinFunction.from(functionExpressionList);
    }

    /**
     * 字符串Join函数
     * #join(连接符,待连接字符)
     */
    public static class JoinFunction extends AbstractFunctionExpression {

        private JoinFunction(final List<Expression> functionExpressionList) {
            super(functionExpressionList);
        }

        private static JoinFunctionFactory.JoinFunction from(final List<Expression> functionExpressionList) {
            return new JoinFunctionFactory.JoinFunction(functionExpressionList);
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