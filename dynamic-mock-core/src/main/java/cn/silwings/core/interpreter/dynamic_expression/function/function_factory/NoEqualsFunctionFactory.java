package cn.silwings.core.interpreter.dynamic_expression.function.function_factory;

import org.springframework.stereotype.Component;
import cn.silwings.core.exceptions.DynamicValueCompileException;
import cn.silwings.core.handler.context.MockHandlerContext;
import cn.silwings.core.interpreter.ExpressionTreeNode;
import cn.silwings.core.interpreter.dynamic_expression.function.AbstractFunctionExpression;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionInfo;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionReturnType;
import cn.silwings.core.utils.CheckUtils;

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
            .functionReturnType(FunctionReturnType.BOOLEAN)
            .description("比较两个字符串是否不相等。支持别名 neq。接收2个参数，返回布尔值。")
            .example("#NoEquals(#Search($.status), 'inactive')\n" +
                    "#neq(#Search($.role), 'admin')")
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
        return "neq".equalsIgnoreCase(methodName) || "noEquals".equalsIgnoreCase(methodName);
    }

    @Override
    public NoEqualsFunction buildFunction(final List<ExpressionTreeNode> functionExpressionList) {
        CheckUtils.sizeBetween(functionExpressionList, NO_EQUALS_FUNCTION_INFO.getMinArgsNumber(), NO_EQUALS_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of NoEquals function."));
        return NoEqualsFunction.from(functionExpressionList, this.equalsFunctionFactory.buildFunction(functionExpressionList));
    }

    /**
     * 相等函数
     * #noEqual(待检查字符A,待检查字符B)
     * #neq(待检查字符A,待检查字符B)
     */
    public static class NoEqualsFunction extends AbstractFunctionExpression {

        private final EqualsFunctionFactory.EqualsFunction equalsFunction;

        private NoEqualsFunction(final List<ExpressionTreeNode> functionExpressionList, final EqualsFunctionFactory.EqualsFunction equalsFunction) {
            super(functionExpressionList);
            this.equalsFunction = equalsFunction;
        }

        public static NoEqualsFunction from(final List<ExpressionTreeNode> functionExpressionList, final EqualsFunctionFactory.EqualsFunction equalsFunction) {
            return new NoEqualsFunction(functionExpressionList, equalsFunction);
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