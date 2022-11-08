package top.silwings.core.dynamic.function.functions;

import top.silwings.core.ParameterContext;
import top.silwings.core.dynamic.DynamicValue;
import top.silwings.core.dynamic.function.AbstractFunctionDynamicValue;
import top.silwings.core.dynamic.function.FunctionFactory;
import top.silwings.core.exceptions.DynamicDataException;

import java.util.List;

/**
 * @ClassName SearchFunctionFactory
 * @Description 搜索函数工厂
 * @Author Silwings
 * @Date 2022/11/8 22:20
 * @Since
 **/
public class SearchFunctionFactory implements FunctionFactory {

    @Override
    public boolean support(final String methodName) {
        return "search".equalsIgnoreCase(methodName);
    }

    @Override
    public SearchFunction buildFunction(final DynamicValue param) {
        return SearchFunction.from(param);
    }

    public static class SearchFunction extends AbstractFunctionDynamicValue {

        public SearchFunction(final DynamicValue param) {
            super(param);
        }

        public static SearchFunction from(final DynamicValue param) {
            return new SearchFunction(param);
        }

        @Override
        public Object value(final ParameterContext parameterContext) {

            final List<Object> paramList = this.getParams(parameterContext);
            if (paramList.isEmpty()) {
                throw new DynamicDataException("缺少搜索词.");
            }

            return parameterContext.searchParameter(String.valueOf(paramList.get(0)));
        }

    }

}