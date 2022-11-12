package top.silwings.core.handler.dynamic.function.functions;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicDataException;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.function.FunctionFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SearchFunctionFactory
 * @Description 搜索函数工厂
 * @Author Silwings
 * @Date 2022/11/8 22:20
 * @Since
 **/
@Component
public class SearchFunctionFactory implements FunctionFactory {

    @Override
    public boolean support(final String methodName) {
        return "search".equalsIgnoreCase(methodName);
    }

    @Override
    public SearchFunction buildFunction(final DynamicValue param) {
        return SearchFunction.from(param);
    }

    public static class SearchFunction extends AbstractDynamicValue {

        // 搜索范围
        // TODO_Silwings: 2022/11/9  
        private static final List<String> searchScope = new ArrayList<>();

        public SearchFunction(final DynamicValue param) {
            super(param);
        }

        public static SearchFunction from(final DynamicValue param) {
            return new SearchFunction(param);
        }

        @Override
        public Object interpret(final Context parameterContext) {

            final List<Object> paramList = this.getParams(parameterContext);
            if (paramList.isEmpty()) {
                throw new DynamicDataException("缺少搜索词.");
            }

            return parameterContext.getParameterContext().searchParameter(String.valueOf(paramList.get(0)));
        }

        @Override
        public Object interpret(final Context context, final List<Object> childNodeValueList) {
            if (childNodeValueList.isEmpty() || childNodeValueList.size() < this.getNodeCount()) {
                throw new DynamicDataException("缺少搜索词.");
            }

            return context.getParameterContext().searchParameter(String.valueOf(childNodeValueList.get(0)));
        }

        @Override
        public int getNodeCount() {
            return this.getChildNodes().size();
        }
    }

}