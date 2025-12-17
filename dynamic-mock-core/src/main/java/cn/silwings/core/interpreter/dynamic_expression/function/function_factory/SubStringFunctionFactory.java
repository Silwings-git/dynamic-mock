package cn.silwings.core.interpreter.dynamic_expression.function.function_factory;

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
import cn.silwings.core.utils.ConvertUtils;
import cn.silwings.core.utils.TypeUtils;

import java.util.List;

/**
 * @ClassName SubStringFunctionFactory
 * @Description 字符串截取
 * @Author Silwings
 * @Date 2023/11/19 14:34
 * @Since
 **/
@Component
public class SubStringFunctionFactory implements FunctionFactory {

    private static final FunctionInfo SUB_STRING_INFO = FunctionInfo.builder()
            .functionName("subString")
            .minArgsNumber(3)
            .maxArgsNumber(3)
            .functionReturnType(FunctionReturnType.STRING)
            .description("字符串截取函数，按索引位置截取字符串的子串。接收3个参数：目标字符串、起始索引（从0开始）、结束索引。" +
                    "函数会自动修正无效的索引值：负数索引会被修正为0，超出长度的索引会被修正为字符串长度，" +
                    "如果结束索引小于起始索引，则会被修正为起始索引（返回空串）。")
            .example("#SubString('hello world', 0, 5) => 'hello'\n" +
                    "#SubString('hello world', 6, 11) => 'world'\n" +
                    "#SubString(#Search($.username), 0, 3)\n" +
                    "#SubString('test', -1, 100) => 'test' (索引自动修正)")
            .build();

    private static final String SYMBOL = "#subString(str,beginIndex,endIndex)";

    @Override
    public boolean support(final String methodName) {
        return "substring".equalsIgnoreCase(methodName);
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return SUB_STRING_INFO;
    }

    @Override
    public SubStringFunction buildFunction(final List<ExpressionTreeNode> expressionList) {
        CheckUtils.sizeBetween(expressionList, SUB_STRING_INFO.getMinArgsNumber(), SUB_STRING_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of SubString function."));
        return SubStringFunction.from(expressionList);
    }

    /**
     * 按位截取字符串
     * #subString(str,beginIndex,endIndex)
     * str: 要截取的目标字符串
     * beginIndex: 截取的起始索引(第一位为0),不应小于0,不应大于str的长度
     * endIndex: 截取的截止索引(第一位为0),不应小于beginIndex,不应大于str的长度+1.如果endIndex等于beginIndex将得到空串
     * 示例:
     * #subString('abc',0,3) => abc
     * #subString('abc',0,2) => ab
     * #subString('abc',1,2) => b
     * #subString('abc',2,2) => "" (这里出现的""并不会在返回值中出现,只是在这里用来演示空串)
     * <p>
     * 该函数将自动将无效的beginIndex或endIndex回正到距离给定值最小的距离的有效值.
     * #subString('abc',-1,3) =自动转换=> #subString('abc',0,3) => abc
     * #subString('abc',-1,300) =自动转换=> #subString('abc',0,3) => abc
     * #subString('abc',100,-11) =自动转换=> #subString('abc',0,0) => "" (空串)
     * #subString('abc',2,-11) =自动转换=> #subString('abc',2,2) => "" (空串)
     * #subString('abc',20,-11) =自动转换=> #subString('abc',3,3) => "" (空串)
     */
    public static class SubStringFunction extends AbstractFunctionExpression {

        protected SubStringFunction(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        public static SubStringFunction from(final List<ExpressionTreeNode> expressionList) {
            return new SubStringFunction(expressionList);
        }

        @Override
        protected String doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            if (childNodeValueList.size() != this.getNodeCount()) {
                throw new DynamicMockException("Parameter incorrectly of `SubString` function. expect: " + SUB_STRING_INFO.getMinArgsNumber() + ", actual: " + childNodeValueList.size());
            }

            final String str = String.valueOf(ConvertUtils.getNoNullOrDefault(childNodeValueList.get(0), ""));
            int start = ConvertUtils.getNoNullOrDefault(TypeUtils.toInteger(childNodeValueList.get(1)), 0);
            int end = ConvertUtils.getNoNullOrDefault(TypeUtils.toInteger(childNodeValueList.get(2)), 0);

            if (start < 0) {
                start = 0;
            } else if (start > str.length()) {
                start = str.length();
            }

            if (end > str.length()) {
                end = str.length();
            } else if (end < 0 || end < start) {
                end = start;
            }

            return str.substring(start, end);
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}