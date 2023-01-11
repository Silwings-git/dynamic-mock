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

import java.util.Collection;
import java.util.List;

/**
 * @ClassName ContainsFunctionFactory
 * @Description 包含函数
 * @Author Silwings
 * @Date 2022/12/29 14:58
 * @Since
 **/
@Component
public class ContainsFunctionFactory implements FunctionFactory {

    private static final FunctionInfo CONTAINS_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("Contains")
            .minArgsNumber(1)
            .maxArgsNumber(2)
            .functionReturnType(FunctionReturnType.BOOLEAN)
            .build();

    private static final String SYMBOL = "#contains(...)";

    @Override
    public boolean support(final String methodName) {
        return "contains".equalsIgnoreCase(methodName);
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return CONTAINS_FUNCTION_INFO;
    }

    @Override
    public ContainsFunction buildFunction(final List<ExpressionTreeNode> functionExpressionList) {
        CheckUtils.sizeBetween(functionExpressionList, CONTAINS_FUNCTION_INFO.getMinArgsNumber(), CONTAINS_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of Contains function."));
        return ContainsFunction.from(functionExpressionList);
    }

    /**
     * 包含函数
     * #contains(元素集,被包含元素)
     * #contains(元素集,被包含元素集)
     * #contains(字符串,被包含元素)
     */
    public static class ContainsFunction extends AbstractFunctionExpression {

        public ContainsFunction(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        public static ContainsFunction from(final List<ExpressionTreeNode> functionExpressionList) {
            return new ContainsFunction(functionExpressionList);
        }

        @Override
        protected Boolean doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            if (childNodeValueList.size() < 2) {
                return false;
            }

            final Object container = childNodeValueList.get(0);
            final Object included = childNodeValueList.get(1);

            if (null == container) {
                return false;
            }

            if (container instanceof Collection) {

                if (included instanceof Collection) {

                    return CollectionUtils.containsAll((Collection<?>) container, (Collection<?>) included);
                }

                return ((Collection<?>) container).contains(included);

            } else if (container instanceof String) {

                if (included instanceof String) {

                    return ((String) container).contains((String) included);
                }

                return false;
            }

            throw DynamicMockException.from("Wrong parameter type.require String/List ,actual : " + container.getClass().getName());
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }
}