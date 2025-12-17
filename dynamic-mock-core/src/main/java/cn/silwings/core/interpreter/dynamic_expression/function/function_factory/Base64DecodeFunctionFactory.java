package cn.silwings.core.interpreter.dynamic_expression.function.function_factory;

import cn.silwings.core.exceptions.DynamicValueCompileException;
import cn.silwings.core.handler.context.MockHandlerContext;
import cn.silwings.core.interpreter.ExpressionTreeNode;
import cn.silwings.core.interpreter.dynamic_expression.function.AbstractFunctionExpression;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionInfo;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionReturnType;
import cn.silwings.core.utils.CheckUtils;
import cn.silwings.core.utils.TypeUtils;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;

/**
 * @ClassName URLEncodeFunctionFactory
 * @Description Url
 * @Author Silwings
 * @Date 2023/5/26 19:52
 * @Since
 **/
@Component
public class Base64DecodeFunctionFactory implements FunctionFactory {

    private static final FunctionInfo BASE64_DECODE_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("Base64Decode")
            .minArgsNumber(1)
            .maxArgsNumber(2)
            .description("Base64解码函数，用于将Base64编码的字符串解码为原始数据。支持2种调用方式：\n" +
                    "1. #Base64Decode(encodedData) - 解码后返回字符串\n" +
                    "2. #Base64Decode(encodedData, returnString) - 指定返回类型，returnString为true返回字符串，false返回字节数组")
            .example("#Base64Decode('SGVsbG8gV29ybGQ=')\n" +
                    "#Base64Decode('5rWL6K+V5pWw5o2u')\n" +
                    "#Base64Decode(#Search($.encodedToken), true)")
            .functionReturnType(FunctionReturnType.STRING)
            .build();

    private static final String SYMBOL = "#base64Decode(...)";

    @Override
    public boolean support(final String methodName) {
        return "base64decode".equalsIgnoreCase(methodName);
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return BASE64_DECODE_FUNCTION_INFO;
    }

    @Override
    public Base64DecodeFunction buildFunction(final List<ExpressionTreeNode> expressionList) {
        CheckUtils.sizeBetween(expressionList, BASE64_DECODE_FUNCTION_INFO.getMinArgsNumber(), BASE64_DECODE_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of Base64Encode function."));
        return Base64DecodeFunction.from(expressionList);
    }

    public static class Base64DecodeFunction extends AbstractFunctionExpression {

        protected Base64DecodeFunction(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        public static Base64DecodeFunction from(final List<ExpressionTreeNode> expressionList) {
            return new Base64DecodeFunction(expressionList);
        }

        @Override
        protected Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            final Object obj = childNodeValueList.get(0);
            if (null == obj) {
                return null;
            }

            boolean returnString = true;
            if (childNodeValueList.size() > 1) {
                returnString = TypeUtils.toBooleanValue(childNodeValueList.get(1));
            }

            try {

                byte[] bytes;

                if (obj instanceof byte[]) {
                    bytes = Base64.getDecoder().decode((byte[]) obj);
                } else {
                    bytes = Base64.getDecoder().decode(String.valueOf(obj));
                }

                return returnString ? new String(bytes) : bytes;

            } catch (Exception e) {
                return obj;
            }
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}