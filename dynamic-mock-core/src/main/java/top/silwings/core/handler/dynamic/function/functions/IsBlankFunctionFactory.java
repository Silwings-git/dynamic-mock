package top.silwings.core.handler.dynamic.function.functions;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.function.AbstractFunctionDynamicValue;
import top.silwings.core.handler.dynamic.function.FunctionFactory;

import java.util.List;

/**
 * @ClassName SearchFunctionFactory
 * @Description 搜索函数工厂
 * @Author Silwings
 * @Date 2022/11/8 22:20
 * @Since
 **/
@Component
public class IsBlankFunctionFactory implements FunctionFactory {

    @Override
    public boolean support(final String methodName) {
        return "isBlank".equalsIgnoreCase(methodName);
    }

    @Override
    public IsBlankFunction buildFunction(final DynamicValue param) {
        return IsBlankFunction.from(param);
    }

    public static class IsBlankFunction extends AbstractFunctionDynamicValue {

        public IsBlankFunction(final DynamicValue param) {
            super(param);
        }

        public static IsBlankFunction from(final DynamicValue param) {
            return new IsBlankFunction(param);
        }

        @Override
        public Boolean interpret(final Context parameterContext) {

            final List<Object> paramList = this.getParams(parameterContext);

            return CollectionUtils.isEmpty(paramList)
                    || null == paramList.get(0)
                    || (paramList.get(0) instanceof String
                    && StringUtils.isBlank((CharSequence) paramList.get(0)));
        }

    }

}