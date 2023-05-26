package top.silwings.core.interpreter.dynamic_expression.function.function_factory;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.context.MockHandlerContext;
import top.silwings.core.interpreter.ExpressionTreeNode;
import top.silwings.core.interpreter.dynamic_expression.function.AbstractFunctionExpression;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionExpression;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionInfo;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionReturnType;
import top.silwings.core.utils.CheckUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @ClassName URLEncodeFunctionFactory
 * @Description Url
 * @Author Silwings
 * @Date 2023/5/26 19:52
 * @Since
 **/
@Component
public class URLDecodeFunctionFactory implements FunctionFactory {

    private static final FunctionInfo URL_DECODE_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("UrlDecode")
            .minArgsNumber(1)
            .maxArgsNumber(1)
            .functionReturnType(FunctionReturnType.STRING)
            .build();

    private static final String SYMBOL = "#urlDecode(...)";

    @Override
    public boolean support(final String methodName) {
        return "urldecode".equalsIgnoreCase(methodName);
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return URL_DECODE_FUNCTION_INFO;
    }

    @Override
    public FunctionExpression buildFunction(final List<ExpressionTreeNode> expressionList) {
        CheckUtils.sizeBetween(expressionList, URL_DECODE_FUNCTION_INFO.getMinArgsNumber(), URL_DECODE_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of UrlDecode function."));
        return URLDecodeFunction.from(expressionList);
    }

    public static class URLDecodeFunction extends AbstractFunctionExpression{

        protected URLDecodeFunction(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        public static URLDecodeFunction from(final List<ExpressionTreeNode> expressionList) {
            return new URLDecodeFunction(expressionList);
        }

        @Override
        protected Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            final Object obj = childNodeValueList.get(0);

            try {
                return URLDecoder.decode(String.valueOf(obj), StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                return obj;
            }
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}