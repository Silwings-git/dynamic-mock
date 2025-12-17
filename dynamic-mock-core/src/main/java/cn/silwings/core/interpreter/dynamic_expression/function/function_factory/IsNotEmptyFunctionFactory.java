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
 * @ClassName IsEmptyFunctionFactory
 * @Description 集合判空函数工厂
 * @Author Silwings
 * @Date 2022/11/8 22:20
 * @Since
 **/
@Component
public class IsNotEmptyFunctionFactory implements FunctionFactory {

    private static final FunctionInfo IS_NOT_EMPTY_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("IsNotEmpty")
            .minArgsNumber(0)
            .maxArgsNumber(1)
            .functionReturnType(FunctionReturnType.BOOLEAN)
            .description("判断集合或映射是否不为空。支持List、Map类型及其JSON字符串表示。参数不为空且集合/映射包含元素时返回true。")
            .example("#IsNotEmpty(#Search($.roles))\n" +
                    "#IsNotEmpty(#Search($.config))")
            .build();

    private static final String SYMBOL = "#isNotEmpty(...)";

    private final IsEmptyFunctionFactory isEmptyFunctionFactory;

    public IsNotEmptyFunctionFactory(final IsEmptyFunctionFactory isEmptyFunctionFactory) {
        this.isEmptyFunctionFactory = isEmptyFunctionFactory;
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return IS_NOT_EMPTY_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "isNotEmpty".equalsIgnoreCase(methodName);
    }

    @Override
    public IsNotEmptyFunction buildFunction(final List<ExpressionTreeNode> functionExpressionList) {
        CheckUtils.sizeBetween(functionExpressionList, IS_NOT_EMPTY_FUNCTION_INFO.getMinArgsNumber(), IS_NOT_EMPTY_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of IsNotEmpty function."));
        return IsNotEmptyFunction.from(functionExpressionList, this.isEmptyFunctionFactory);
    }

    /**
     * 集合判空函数
     * #isNotEmpty(待检查集合)
     * 如果参数不为空且不是集合，映射，或可转换为集合，映射的字符串类型，抛出异常
     */
    public static class IsNotEmptyFunction extends AbstractFunctionExpression {

        private final IsEmptyFunctionFactory.IsEmptyFunction isEmptyFunction;

        private IsNotEmptyFunction(final List<ExpressionTreeNode> functionExpressionList, final IsEmptyFunctionFactory.IsEmptyFunction isEmptyFunction) {
            super(functionExpressionList);
            this.isEmptyFunction = isEmptyFunction;
        }

        private static IsNotEmptyFunction from(final List<ExpressionTreeNode> functionExpressionList, final IsEmptyFunctionFactory isEmptyFunctionFactory) {
            return new IsNotEmptyFunction(functionExpressionList, isEmptyFunctionFactory.buildFunction(functionExpressionList));
        }

        @Override
        public Boolean doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            return !this.isEmptyFunction.doInterpret(mockHandlerContext, childNodeValueList);
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}