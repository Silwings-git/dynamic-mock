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
import cn.silwings.core.utils.JsonUtils;

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
            .description("JSON字符串转对象函数。将JSON格式的字符串解析为Java对象（Map或List）。")
            .example("#ToBean('{\"name\":\"John\",\"age\":30}')\n" +
                    "#ToBean(#Search($.jsonString))")
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