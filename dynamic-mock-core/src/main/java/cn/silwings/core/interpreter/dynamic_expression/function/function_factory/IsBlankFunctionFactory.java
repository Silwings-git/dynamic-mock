package cn.silwings.core.interpreter.dynamic_expression.function.function_factory;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
public class IsBlankFunctionFactory implements FunctionFactory {

    private static final FunctionInfo IS_BLANK_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("IsBlank")
            .minArgsNumber(0)
            .maxArgsNumber(1)
            .functionReturnType(FunctionReturnType.BOOLEAN)
            .description("判断字符串是否为空。参数为空、null或只包含空白字符时返回true，否则返回false。")
            .example("#IsBlank(#Search($.name))\n" +
                    "#IsBlank(#Search($.description))")
            .build();

    private static final String SYMBOL = "#isBlank(...)";

    @Override
    public FunctionInfo getFunctionInfo() {
        return IS_BLANK_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "isBlank".equalsIgnoreCase(methodName);
    }

    @Override
    public IsBlankFunction buildFunction(final List<ExpressionTreeNode> functionExpressionList) {
        CheckUtils.sizeBetween(functionExpressionList, IS_BLANK_FUNCTION_INFO.getMinArgsNumber(), IS_BLANK_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of IsBlank function."));
        return IsBlankFunction.from(functionExpressionList);
    }

    /**
     * 判空函数
     * #isBlank(待检查字符)
     */
    public static class IsBlankFunction extends AbstractFunctionExpression {

        private IsBlankFunction(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        private static IsBlankFunction from(final List<ExpressionTreeNode> functionExpressionList) {
            return new IsBlankFunction(functionExpressionList);
        }

        @Override
        public Boolean doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            if (CollectionUtils.isEmpty(childNodeValueList)) {
                return true;
            }

            final Object ele = childNodeValueList.get(0);

            if (null == ele) {
                return true;
            }

            return StringUtils.isBlank(String.valueOf(ele));
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}