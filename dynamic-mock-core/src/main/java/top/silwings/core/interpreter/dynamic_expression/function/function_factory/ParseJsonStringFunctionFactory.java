package top.silwings.core.interpreter.dynamic_expression.function.function_factory;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.config.DynamicMockContext;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.context.MockHandlerContext;
import top.silwings.core.interpreter.ExpressionInterpreter;
import top.silwings.core.interpreter.ExpressionTreeNode;
import top.silwings.core.interpreter.dynamic_expression.function.AbstractFunctionExpression;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionInfo;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionReturnType;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.JsonUtils;
import top.silwings.core.utils.TypeUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName ParseJsonStringFunctionFactory
 * @Description 解析Json格式字符串并执行json中包含的动态表达式
 * @Author Silwings
 * @Date 2023/5/24 21:37
 * @Since
 **/
@Component
public class ParseJsonStringFunctionFactory implements FunctionFactory {

    private static final FunctionInfo PARSE_JSON_STRING_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("ParseJsonString")
            .minArgsNumber(1)
            .maxArgsNumber(2)
            // 实际上,ParseJsonString函数的返回值也可能是FunctionReturnType.List,这里将其也归类到FunctionReturnType.OBJECT
            .functionReturnType(FunctionReturnType.OBJECT)
            .description("解析JSON字符串并执行其中包含的动态表达式。支持2种用法：\n" +
                    "1. #ParseJsonString(jsonString) - 解析JSON字符串，执行其中的动态表达式\n" +
                    "2. #ParseJsonString(jsonString, JsonPath) - 解析JSON并从指定路径获取值")
            .example("#ParseJsonString('{\"time\":\"#Now()\",\"user\":\"#Search($.userId)\"}')\n" +
                    "#ParseJsonString(#Search($.template), '$.data')")
            .build();

    private static final String SYMBOL = "#parseJsonString(...)";


    @Override
    public boolean support(final String methodName) {
        return "parsejsonstring".equalsIgnoreCase(methodName)
               || "parsejsonstr".equalsIgnoreCase(methodName)
               || "pjs".equalsIgnoreCase(methodName);
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return PARSE_JSON_STRING_FUNCTION_INFO;
    }

    @Override
    public ParseJsonStringFunction buildFunction(final List<ExpressionTreeNode> expressionList) {
        CheckUtils.sizeBetween(expressionList, PARSE_JSON_STRING_FUNCTION_INFO.getMinArgsNumber(), PARSE_JSON_STRING_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of ParseJsonString function."));
        return ParseJsonStringFunction.from(expressionList);
    }

    /**
     * json字符串解析函数
     * 解析Json格式字符串并执行json中包含的动态表达式
     * 1.#parseJsonString(json格式字符串)
     * 2.#parseJsonString(json格式字符串,数据是否动态)
     * 不指定第二个参数时默认为true
     * 如果不是动态数据,该函数将尝试将json字符串反序列化为json对象后直接返回
     * 如果是动态数据,将进一步对json对象进行解析,并执行相关的动态表达式
     * 如果json字符串格式错误,无法成功反序列化为json对象,将抛出错误,这里的json格式对象支持对象和数组两种格式.
     */
    public static class ParseJsonStringFunction extends AbstractFunctionExpression {

        protected ParseJsonStringFunction(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        public static ParseJsonStringFunction from(final List<ExpressionTreeNode> expressionList) {
            return new ParseJsonStringFunction(expressionList);
        }

        @Override
        protected Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            if (CollectionUtils.isEmpty(childNodeValueList)) {
                return new HashMap<>();
            }

            final Object jsonObject = this.parseObjectOrArray(childNodeValueList.get(0));

            // 当指定为非动态数据时,直接尝试反序列化
            if (childNodeValueList.size() > 1 && !TypeUtils.toBooleanValue(childNodeValueList.get(1))) {
                return jsonObject;
            }

            // 进一步进行动态表达式解析与计算
            return this.buildNodeInterpreter(jsonObject).interpret(mockHandlerContext);
        }

        private Object parseObjectOrArray(final Object jsonArg) {

            if (jsonArg instanceof String) {
                return JsonUtils.toBean((String) jsonArg);
            }

            throw DynamicValueCompileException.from("The first parameter must be a valid JSON format string");
        }

        private ExpressionInterpreter buildNodeInterpreter(final Object obj) {
            return new ExpressionInterpreter(DynamicMockContext.getInstance().getJsonTreeParser().parse(obj));
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}