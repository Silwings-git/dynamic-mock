package top.silwings.core.interpreter.dynamic_expression.function.function_factory;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.context.MockHandlerContext;
import top.silwings.core.interpreter.ExpressionTreeNode;
import top.silwings.core.interpreter.dynamic_expression.function.AbstractFunctionExpression;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionInfo;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionReturnType;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.TypeUtils;

import java.nio.ByteBuffer;
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
public class Base64DecodeFunctionFactory implements FunctionFactory {

    private static final FunctionInfo BASE64_DECODE_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("Base64Decode")
            .minArgsNumber(1)
            .maxArgsNumber(2)
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

                if (obj instanceof byte[]){
                    bytes = Base64.getDecoder().decode((byte[]) obj);
                }else {
                    bytes= Base64.getDecoder().decode(String.valueOf(obj));
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