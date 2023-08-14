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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
public class URLEncodeFunctionFactory implements FunctionFactory {

    private static final FunctionInfo URL_ENCODE_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("UrlEncode")
            .minArgsNumber(1)
            .maxArgsNumber(1)
            .functionReturnType(FunctionReturnType.STRING)
            .build();

    private static final String SYMBOL = "#urlEncode(...)";

    @Override
    public boolean support(final String methodName) {
        return "urlencode".equalsIgnoreCase(methodName);
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return URL_ENCODE_FUNCTION_INFO;
    }

    @Override
    public URLEncodeFunction buildFunction(final List<ExpressionTreeNode> expressionList) {
        CheckUtils.sizeBetween(expressionList, URL_ENCODE_FUNCTION_INFO.getMinArgsNumber(), URL_ENCODE_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of UrlDecode function."));
        return URLEncodeFunction.from(expressionList);
    }

    public static class URLEncodeFunction extends AbstractFunctionExpression {

        protected URLEncodeFunction(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        public static URLEncodeFunction from(final List<ExpressionTreeNode> expressionList) {
            return new URLEncodeFunction(expressionList);
        }

        @Override
        protected Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            final Object obj = childNodeValueList.get(0);
            if (null == obj) {
                return null;
            }

            try {
                return URLEncoder.encode(String.valueOf(obj), StandardCharsets.UTF_8.toString());
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