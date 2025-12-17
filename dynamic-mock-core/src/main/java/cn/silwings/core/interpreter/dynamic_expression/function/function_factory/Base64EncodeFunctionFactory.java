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
import cn.silwings.core.utils.TypeUtils;

import java.nio.charset.StandardCharsets;
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
public class Base64EncodeFunctionFactory implements FunctionFactory {

    private static final FunctionInfo BASE64_ENCODE_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("Base64Encode")
            .minArgsNumber(1)
            .maxArgsNumber(2)
            .description("Base64编码函数，用于将字符串或字节数组编码为Base64格式。支持2种调用方式：\n" +
                    "1. #Base64Encode(data) - 编码后返回字符串\n" +
                    "2. #Base64Encode(data, returnString) - 指定返回类型，returnString为true返回字符串，false返回字节数组")
            .example("#Base64Encode('Hello World')\n" +
                    "#Base64Encode('测试数据')\n" +
                    "#Base64Encode(#Search($.data), true)")
            .functionReturnType(FunctionReturnType.STRING)
            .build();

    private static final String SYMBOL = "#base64Encode(...)";

    @Override
    public boolean support(final String methodName) {
        return "base64encode".equalsIgnoreCase(methodName);
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return BASE64_ENCODE_FUNCTION_INFO;
    }

    @Override
    public Base64EncodeFunction buildFunction(final List<ExpressionTreeNode> expressionList) {
        CheckUtils.sizeBetween(expressionList, BASE64_ENCODE_FUNCTION_INFO.getMinArgsNumber(), BASE64_ENCODE_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of Base64Encode function."));
        return Base64EncodeFunction.from(expressionList);
    }

    public static class Base64EncodeFunction extends AbstractFunctionExpression {

        protected Base64EncodeFunction(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        public static Base64EncodeFunction from(final List<ExpressionTreeNode> expressionList) {
            return new Base64EncodeFunction(expressionList);
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

                if (obj instanceof byte[]){
                    bytes = Base64.getEncoder().encode((byte[]) obj);
                }else {
                    bytes = Base64.getEncoder().encode(String.valueOf(obj).getBytes(StandardCharsets.UTF_8));
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