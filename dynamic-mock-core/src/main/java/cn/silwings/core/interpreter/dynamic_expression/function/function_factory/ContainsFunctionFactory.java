package cn.silwings.core.interpreter.dynamic_expression.function.function_factory;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import cn.silwings.core.exceptions.DynamicMockException;
import cn.silwings.core.exceptions.DynamicValueCompileException;
import cn.silwings.core.handler.context.MockHandlerContext;
import cn.silwings.core.interpreter.ExpressionTreeNode;
import cn.silwings.core.interpreter.dynamic_expression.function.AbstractFunctionExpression;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionInfo;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionReturnType;
import cn.silwings.core.utils.CheckUtils;

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
            .description("包含判断函数，判断字符串或集合是否包含指定元素。支持3种用法：\n" +
                    "1. #Contains(str) - 判断requestInfo转JSON字符串是否包含str\n" +
                    "2. #Contains(collection, element) - 判断集合是否包含元素\n" +
                    "3. #Contains(str, substring) - 判断字符串str是否包含子串substring")
            .example("#Contains('admin')\n" +
                    "#Contains(#Search($.roles), 'ADMIN')\n" +
                    "#Contains(#Search($.name), 'test')")
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

            throw DynamicMockException.from("Wrong parameter type.require String/List ,actual : " + container.getClass().getName() + ".actual: " + container + ":" + included);
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }
}