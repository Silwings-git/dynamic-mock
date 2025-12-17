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
 * @ClassName IsBlankFunctionFactory
 * @Description 判空函数工厂
 * @Author Silwings
 * @Date 2022/11/8 22:20
 * @Since
 **/
@Component
public class IsNotBlankFunctionFactory implements FunctionFactory {

    private static final FunctionInfo IS_NOT_BLANK_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("IsNotBlank")
            .minArgsNumber(0)
            .maxArgsNumber(1)
            .functionReturnType(FunctionReturnType.BOOLEAN)
            .description("判断字符串是否不为空。参数不为空、不为null且包含非空白字符时返回true，否则返回false。")
            .example("#IsNotBlank(#Search($.email))\n" +
                    "#IsNotBlank(#Search($.username))")
            .build();

    private static final String SYMBOL = "#isNotBlank(...)";

    private final IsBlankFunctionFactory isBlankFunctionFactory;

    public IsNotBlankFunctionFactory(final IsBlankFunctionFactory isBlankFunctionFactory) {
        this.isBlankFunctionFactory = isBlankFunctionFactory;
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return IS_NOT_BLANK_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "isNotBlank".equalsIgnoreCase(methodName);
    }

    @Override
    public IsNotBlankFunction buildFunction(final List<ExpressionTreeNode> functionExpressionList) {
        CheckUtils.sizeBetween(functionExpressionList, IS_NOT_BLANK_FUNCTION_INFO.getMinArgsNumber(), IS_NOT_BLANK_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of IsNotBlank function."));
        return IsNotBlankFunction.from(functionExpressionList, this.isBlankFunctionFactory);
    }

    /**
     * 判空函数
     * #isNotBlank(待检查字符)
     */
    public static class IsNotBlankFunction extends AbstractFunctionExpression {

        private final IsBlankFunctionFactory.IsBlankFunction isBlankFunction;

        private IsNotBlankFunction(final List<ExpressionTreeNode> functionExpressionList, final IsBlankFunctionFactory.IsBlankFunction isBlankFunction) {
            super(functionExpressionList);
            this.isBlankFunction = isBlankFunction;
        }

        private static IsNotBlankFunction from(final List<ExpressionTreeNode> functionExpressionList, final IsBlankFunctionFactory isBlankFunctionFactory) {
            return new IsNotBlankFunction(functionExpressionList, isBlankFunctionFactory.buildFunction(functionExpressionList));
        }

        @Override
        public Boolean doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            return !this.isBlankFunction.doInterpret(mockHandlerContext, childNodeValueList);
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}