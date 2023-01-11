package top.silwings.core.interpreter.dynamic_expression.function.function_factory;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.interpreter.ExpressionTreeNode;
import top.silwings.core.interpreter.dynamic_expression.function.AbstractFunctionExpression;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionInfo;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionReturnType;
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
            .functionReturnType(FunctionReturnType.OBJECT)
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
    public ToBeanFunction buildFunction(final List<ExpressionTreeNode> functionExpressionList) {
        CheckUtils.sizeBetween(functionExpressionList, TO_BEAN_FUNCTION_INFO.getMinArgsNumber(), TO_BEAN_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of ToBean function."));
        return ToBeanFunction.form(functionExpressionList);
    }

    public static class ToBeanFunction extends AbstractFunctionExpression {

        protected ToBeanFunction(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        public static ToBeanFunction form(final List<ExpressionTreeNode> functionExpressionList) {
            return new ToBeanFunction(functionExpressionList);
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