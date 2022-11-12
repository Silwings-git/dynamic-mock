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
    public DynamicValue buildFunction(final List<DynamicValue> dynamicValueList) {
        return SearchFunction.from(dynamicValueList);
    }

    public static class SearchFunction extends AbstractDynamicValue {

        // 搜索范围
        // TODO_Silwings: 2022/11/9  
        private static final List<String> searchScope = new ArrayList<>();

        public SearchFunction(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        public static SearchFunction from(final List<DynamicValue> dynamicValueList) {
            return new SearchFunction(dynamicValueList);
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