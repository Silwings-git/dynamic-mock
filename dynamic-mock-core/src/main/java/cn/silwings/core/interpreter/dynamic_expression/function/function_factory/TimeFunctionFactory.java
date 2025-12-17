package cn.silwings.core.interpreter.dynamic_expression.function.function_factory;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import cn.silwings.core.exceptions.DynamicValueCompileException;
import cn.silwings.core.handler.context.MockHandlerContext;
import cn.silwings.core.interpreter.ExpressionTreeNode;
import cn.silwings.core.interpreter.dynamic_expression.function.AbstractFunctionExpression;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionExpression;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionInfo;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionReturnType;
import cn.silwings.core.utils.CheckUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * @ClassName TimeFunctionFactory
 * @Description 参考jmeter的time函数
 * @Author Silwings
 * @Date 2023/8/15 18:28
 * @Since
 **/
@Component
public class TimeFunctionFactory implements FunctionFactory {

    private static final FunctionInfo TIME_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("Time")
            .minArgsNumber(0)
            .maxArgsNumber(1)
            .functionReturnType(FunctionReturnType.STRING)
            .description("时间函数，获取当前时间并按指定格式输出。支持2种用法：\n" +
                    "1. #Time() - 返回当前时间戳字符串\n" +
                    "2. #Time('format') - 返回指定格式的当前时间字符串，如 'yyyy-MM-dd HH:mm:ss'")
            .example("#Time()\n" +
                    "#Time('yyyy-MM-dd HH:mm:ss')\n" +
                    "#Time('yyyyMMdd')")
            .build();

    private static final String SYMBOL = "time(,,,)";

    @Override
    public boolean support(final String methodName) {
        return "time".equalsIgnoreCase(methodName);
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return TIME_FUNCTION_INFO;
    }

    @Override
    public FunctionExpression buildFunction(final List<ExpressionTreeNode> expressionList) {
        CheckUtils.sizeBetween(expressionList, TIME_FUNCTION_INFO.getMinArgsNumber(), TIME_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of StartsWith function."));
        return TimeFunction.from(expressionList);
    }

    public static class TimeFunction extends AbstractFunctionExpression {

        private static final Pattern DIVISOR_PATTERN = Pattern.compile("/\\d+");

        // Only modified in class init
        private static final Map<String, String> aliases = new HashMap<>();

        private static final LoadingCache<String, Supplier<String>> DATE_TIME_FORMATTER_CACHE =
                Caffeine.newBuilder()
                        .maximumSize(1000)
                        .build((fmt) -> {
                            if (DIVISOR_PATTERN.matcher(fmt).matches()) {
                                // should never case NFE
                                long div = Long.parseLong(fmt.substring(1));
                                return () -> Long.toString(System.currentTimeMillis() / div);
                            }
                            DateTimeFormatter df = DateTimeFormatter
                                    .ofPattern(fmt)
                                    .withZone(ZoneId.systemDefault());
                            return () -> df.format(Instant.now());
                        });

        protected TimeFunction(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        public static TimeFunction from(final List<ExpressionTreeNode> functionExpressionList) {
            return new TimeFunction(functionExpressionList);
        }

        @Override
        protected Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            final String format = CollectionUtils.isEmpty(childNodeValueList) ? null : String.valueOf(childNodeValueList.get(0));

            String datetime;
            if (StringUtils.isBlank(format)) {
                datetime = Long.toString(System.currentTimeMillis());
            } else {
                // Resolve any aliases
                String fmt = aliases.get(format);
                if (fmt == null) {
                    fmt = format;// Not found
                }
                datetime = DATE_TIME_FORMATTER_CACHE.get(fmt).get();
            }

            return datetime;
        }

        static {
            //$NON-NLS-1$
            aliases.put("YMD", "yyyyMMdd");
            //$NON-NLS-1$
            aliases.put("HMS", "HHmmss");
            //$NON-NLS-1$
            aliases.put("YMDHMS", "yyyyMMdd-HHmmss");
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}