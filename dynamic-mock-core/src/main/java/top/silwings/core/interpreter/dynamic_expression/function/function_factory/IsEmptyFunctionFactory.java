package top.silwings.core.interpreter.dynamic_expression.function.function_factory;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.interpreter.ExpressionTreeNode;
import top.silwings.core.interpreter.dynamic_expression.function.AbstractFunctionExpression;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionInfo;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionReturnType;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.JsonUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @ClassName IsEmptyFunctionFactory
 * @Description 集合判空函数工厂
 * @Author Silwings
 * @Date 2022/11/8 22:20
 * @Since
 **/
@Component
public class IsEmptyFunctionFactory implements FunctionFactory {

    private static final FunctionInfo IS_EMPTY_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("IsEmpty")
            .minArgsNumber(0)
            .maxArgsNumber(1)
            .functionReturnType(FunctionReturnType.BOOLEAN)
            .build();

    private static final String SYMBOL = "#isEmpty(...)";

    @Override
    public FunctionInfo getFunctionInfo() {
        return IS_EMPTY_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "isEmpty".equalsIgnoreCase(methodName);
    }

    @Override
    public IsEmptyFunction buildFunction(final List<ExpressionTreeNode> functionExpressionList) {
        CheckUtils.sizeBetween(functionExpressionList, IS_EMPTY_FUNCTION_INFO.getMinArgsNumber(), IS_EMPTY_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of IsEmpty function."));
        return IsEmptyFunction.from(functionExpressionList);
    }

    /**
     * 集合判空函数
     * #isEmpty(待检查集合)
     * 如果参数不为空且不是集合，映射，或可转换为集合，映射的字符串类型，抛出异常
     */
    public static class IsEmptyFunction extends AbstractFunctionExpression {

        private IsEmptyFunction(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        private static IsEmptyFunction from(final List<ExpressionTreeNode> functionExpressionList) {
            return new IsEmptyFunction(functionExpressionList);
        }

        @Override
        public Boolean doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            if (CollectionUtils.isEmpty(childNodeValueList)) {
                return true;
            }

            Object ele = childNodeValueList.get(0);

            if (null == ele) {
                return true;
            }

            if (ele instanceof String && JsonUtils.isValidJson((String) ele)) {
                ele = JsonUtils.toBean((String) ele);
            }

            if (ele instanceof List) {
                return CollectionUtils.isEmpty((Collection<?>) ele);
            }

            if (ele instanceof Map) {
                return MapUtils.isEmpty((Map<?, ?>) ele);
            }

            throw DynamicMockException.from("Wrong parameter type.require List/Map ,actual : " + ele.getClass().getName());
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}