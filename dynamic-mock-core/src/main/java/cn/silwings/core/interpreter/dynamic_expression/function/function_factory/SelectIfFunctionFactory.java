package cn.silwings.core.interpreter.dynamic_expression.function.function_factory;

import org.springframework.stereotype.Component;
import cn.silwings.core.exceptions.DynamicValueCompileException;
import cn.silwings.core.handler.context.MockHandlerContext;
import cn.silwings.core.interpreter.ExpressionTreeNode;
import cn.silwings.core.interpreter.dynamic_expression.function.AbstractFunctionExpression;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionExpression;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionInfo;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionReturnType;
import cn.silwings.core.utils.CheckUtils;
import cn.silwings.core.utils.TypeUtils;

import java.util.List;

/**
 * @ClassName SelectIfFunctionFactory
 * @Description 选择函数(三元运算符)
 * @Author Silwings
 * @Date 2023/1/8 22:21
 * @Since
 **/
@Component
public class SelectIfFunctionFactory implements FunctionFactory {

    private static final FunctionInfo SELECT_IF_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("SelectIf")
            .minArgsNumber(3)
            .maxArgsNumber(3)
            .functionReturnType(FunctionReturnType.OBJECT)
            .description("三元运算符函数，根据条件选择返回不同的值。接收3个参数：条件表达式、条件为true时的返回值、条件为false时的返回值。")
            .example("#SelectIf(#Equals(#Search($.role), 'admin'), 'Administrator', 'User')\n" +
                    "#SelectIf(#IsNotNull(#Search($.vip)), '会员', '普通用户')")
            .build();

    private static final String SYMBOL = "#selectIf(...)";

    @Override
    public boolean support(final String methodName) {
        return "selectIf".equalsIgnoreCase(methodName);
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return SELECT_IF_FUNCTION_INFO;
    }

    @Override
    public FunctionExpression buildFunction(final List<ExpressionTreeNode> functionExpressionList) {
        CheckUtils.sizeBetween(functionExpressionList, SELECT_IF_FUNCTION_INFO.getMinArgsNumber(), SELECT_IF_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of SelectIf function."));
        return SelectIfFunction.from(functionExpressionList);
    }

    /**
     * 选择函数
     * 等于三元运算表达式
     * #selectIf(boolean,argA,argB)
     */
    public static class SelectIfFunction extends AbstractFunctionExpression {

        public SelectIfFunction(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        public static SelectIfFunction from(final List<ExpressionTreeNode> functionExpressionList) {
            return new SelectIfFunction(functionExpressionList);
        }

        @Override
        protected Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            return Boolean.TRUE.equals(TypeUtils.toBooleanValue(childNodeValueList.get(0))) ? childNodeValueList.get(1) : childNodeValueList.get(2);
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }

    }

}