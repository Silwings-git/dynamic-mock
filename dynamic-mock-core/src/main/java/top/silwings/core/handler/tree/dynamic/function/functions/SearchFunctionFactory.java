package top.silwings.core.handler.tree.dynamic.function.functions;

import com.alibaba.fastjson.JSONPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.tree.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.function.FunctionFactory;

import java.util.List;

/**
 * @ClassName SearchFunctionFactory
 * @Description 搜索函数工厂
 * @Author Silwings
 * @Date 2022/11/8 22:20
 * @Since
 **/
@Slf4j
@Component
public class SearchFunctionFactory implements FunctionFactory {

    private static final String SYMBOL = "#search(...)";

    @Override
    public boolean support(final String methodName) {
        return "search".equalsIgnoreCase(methodName);
    }

    @Override
    public DynamicValue buildFunction(final List<DynamicValue> dynamicValueList) {
        return SearchFunction.from(dynamicValueList);
    }

    /**
     * 搜索函数
     * #search(搜索词)
     * #search(搜索词,搜索范围)
     */
    public static class SearchFunction extends AbstractDynamicValue {

        public SearchFunction(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        public static SearchFunction from(final List<DynamicValue> dynamicValueList) {
            return new SearchFunction(dynamicValueList);
        }

        @Override
        public Object doInterpret(final Context context, final List<Object> childNodeValueList) {
            if (childNodeValueList.isEmpty() || childNodeValueList.size() < this.getNodeCount()) {
                throw new DynamicMockException("Parameter incorrectly of `search` function. expect: 1 or 2, actual: " + childNodeValueList.size());
            }

            SearchScope searchScope = null;

            if (childNodeValueList.size() > 1) {
                searchScope = SearchScope.valueOfName(String.valueOf(childNodeValueList.get(1)));
                if (null == searchScope) {
                    return null;
                }
            }

            try {
                if (this.getNodeCount() < 2 || SearchScope.CUSTOMIZESPACE.equals(searchScope)) {

                    // 自定义空间内查询
                    return JSONPath.eval(context.getRequestContext().getCustomizeSpace(), String.valueOf(childNodeValueList.get(0)));
                } else {

                    // 请求信息内查询
                    return JSONPath.eval(context.getRequestContext().getRequestInfo(), String.valueOf(childNodeValueList.get(0)));
                }
            } catch (Exception e) {
                log.error("Json path error.", e);
                return null;
            }
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

    public enum SearchScope {
        // 自定义空间
        CUSTOMIZESPACE,
        // 请求信息
        REQUESTINFO,
        ;

        public static SearchScope valueOfName(final String str) {
            if (StringUtils.isBlank(str)) {
                return null;
            }
            for (final SearchScope value : values()) {
                if (value.name().equalsIgnoreCase(str)) {
                    return value;
                }
            }
            return null;
        }
    }

}