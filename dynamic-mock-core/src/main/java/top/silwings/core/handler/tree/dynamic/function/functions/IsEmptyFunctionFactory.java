package top.silwings.core.handler.tree.dynamic.function.functions;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.handler.tree.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.SingleApostropheText;
import top.silwings.core.handler.tree.dynamic.function.FunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.FunctionInfo;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.JsonUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @ClassName IsEmptyFunctionFactory
 * @Description 集合判空函数工厂
 * @Author Silwings
 * @Date 2022/11/8 22:20
 * @Since
 **/
@Component
public class IsEmptyFunctionFactory implements FunctionFactory {

    private static final FunctionInfo IS_EMPTY_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("IsEmpty")
            .minArgsNumber(0)
            .maxArgsNumber(1)
            .build();

    private static final String SYMBOL = "#isEmpty(...)";

    @Override
    public FunctionInfo getFunctionInfo() {
        return IS_EMPTY_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "isempty".equalsIgnoreCase(methodName);
    }

    @Override
    public IsEmptyFunction buildFunction(final List<DynamicValue> dynamicValueList) {
        CheckUtils.sizeBetween(dynamicValueList, IS_EMPTY_FUNCTION_INFO.getMinArgsNumber(), IS_EMPTY_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of IsEmpty function."));
        return IsEmptyFunction.from(dynamicValueList);
    }

    /**
     * 集合判空函数
     * #isEmpty(待检查集合)
     * 如果参数不为空且不是集合，映射，或可转换为集合，映射的字符串类型，抛出异常
     */
    public static class IsEmptyFunction extends AbstractDynamicValue {

        private IsEmptyFunction(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        private static IsEmptyFunction from(final List<DynamicValue> dynamicValueList) {
            return new IsEmptyFunction(dynamicValueList);
        }

        @Override
        public Boolean doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            if (CollectionUtils.isEmpty(childNodeValueList)) {
                return true;
            }

            Object ele = childNodeValueList.get(0);

            if (null == ele) {
                return true;
            }

            if (ele instanceof String) {

                final String str = SingleApostropheText.tryGetEscapeText((String) ele);

                if (JsonUtils.isValidJson(str)) {

                    ele = JsonUtils.toBean(str);
                }
            }

            if (ele instanceof List) {
                return CollectionUtils.isEmpty((Collection<?>) ele);
            }

            if (ele instanceof Map) {
                return MapUtils.isEmpty((Map<?, ?>) ele);
            }

            throw DynamicMockException.from("Wrong parameter type.require List/Map ,actual : " + ele.getClass().getName());
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}