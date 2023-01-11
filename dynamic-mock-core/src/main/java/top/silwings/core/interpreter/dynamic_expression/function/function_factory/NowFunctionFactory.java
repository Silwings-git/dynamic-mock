package top.silwings.core.interpreter.dynamic_expression.function.function_factory;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.context.MockHandlerContext;
import top.silwings.core.interpreter.ExpressionTreeNode;
import top.silwings.core.interpreter.dynamic_expression.function.AbstractFunctionExpression;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionInfo;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionReturnType;
import top.silwings.core.utils.CheckUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @ClassName NowFunction
 * @Description 当前时间
 * @Author Silwings
 * @Date 2022/11/18 22:20
 * @Since
 **/
@Component
public class NowFunctionFactory implements FunctionFactory {

    private static final FunctionInfo NOW_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("Now")
            .minArgsNumber(0)
            .maxArgsNumber(1)
            .functionReturnType(FunctionReturnType.OBJECT)
            .build();

    private static final String SYMBOL = "#now()";

    @Override
    public FunctionInfo getFunctionInfo() {
        return NOW_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "now".equalsIgnoreCase(methodName);
    }

    @Override
    public NowFunction buildFunction(final List<ExpressionTreeNode> functionExpressionList) {
        CheckUtils.sizeBetween(functionExpressionList, NOW_FUNCTION_INFO.getMinArgsNumber(), NOW_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of Now function."));
        return NowFunction.from(functionExpressionList);
    }

    /**
     * 时间戳函数
     * #now() - 时间戳
     * #now(format) - 格式错误
     */
    public static class NowFunction extends AbstractFunctionExpression {

        private NowFunction(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        private static NowFunction from(final List<ExpressionTreeNode> functionExpressionList) {
            return new NowFunction(functionExpressionList);
        }

        @Override
        protected Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            if (this.getNodeCount() != childNodeValueList.size()) {
                throw new DynamicMockException("Parameter incorrectly of `now` function. expect: " + this.getNodeCount() + " , actual: " + childNodeValueList.size());
            }

            if (CollectionUtils.isNotEmpty(childNodeValueList)) {
                try {
                    return LocalDateTime.now().format(DateTimeFormatter.ofPattern(String.valueOf(childNodeValueList.get(0))));
                } catch (Exception e) {
                    throw new DynamicMockException("Time format error: " + childNodeValueList.get(0));
                }
            }

            return System.currentTimeMillis();
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }
}