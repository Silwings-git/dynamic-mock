package top.silwings.core.handler.tree.dynamic.function.functions;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.handler.tree.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.function.FunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.FunctionInfo;
import top.silwings.core.utils.CheckUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @ClassName NowFunction
 * @Description 当前时间
 * @Author Silwings
 * @Date 2022/11/18 22:20
 * @Since
 **/
@Component
public class NowFunctionFactory implements FunctionFactory {

    private static final FunctionInfo NOW_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("Now")
            .minArgsNumber(0)
            .maxArgsNumber(1)
            .build();

    private static final String SYMBOL = "#now()";

    @Override
    public FunctionInfo getFunctionInfo() {
        return NOW_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "now".equalsIgnoreCase(methodName);
    }

    @Override
    public DynamicValue buildFunction(final List<DynamicValue> dynamicValueList) {
        return NowFunction.from(dynamicValueList);
    }

    /**
     * 时间戳函数
     * #now() - 时间戳
     * #now(format) - 格式错误
     */
    public static class NowFunction extends AbstractDynamicValue {

        private NowFunction(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        public static NowFunction from(final List<DynamicValue> dynamicValueList) {
            CheckUtils.sizeBetween(dynamicValueList, NOW_FUNCTION_INFO.getMinArgsNumber(), NOW_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of Now function."));
            return new NowFunction(dynamicValueList);
        }

        @Override
        protected Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            if (this.getNodeCount() != childNodeValueList.size()) {
                throw new DynamicMockException("Parameter incorrectly of `now` function. expect: " + this.getNodeCount() + " , actual: " + childNodeValueList.size());
            }

            if (CollectionUtils.isNotEmpty(childNodeValueList)) {
                try {
                    return LocalDateTime.now().format(DateTimeFormatter.ofPattern(String.valueOf(childNodeValueList.get(0))));
                } catch (Exception e) {
                    throw new DynamicMockException("Time format error: " + childNodeValueList.get(0));
                }
            }

            return System.currentTimeMillis();
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }
}