package top.silwings.core.interpreter.dynamic_expression.function.function_factory;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.context.MockHandlerContext;
import top.silwings.core.interpreter.ExpressionTreeNode;
import top.silwings.core.interpreter.dynamic_expression.function.AbstractFunctionExpression;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionInfo;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionReturnType;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.ConvertUtils;
import top.silwings.core.utils.JsonUtils;

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

    private static final FunctionInfo SEARCH_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("Search")
            .minArgsNumber(1)
            .maxArgsNumber(3)
            .functionReturnType(FunctionReturnType.OBJECT)
            .build();
    private static final String SYMBOL = "#search(...)";

    @Override
    public FunctionInfo getFunctionInfo() {
        return SEARCH_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "search".equalsIgnoreCase(methodName);
    }

    @Override
    public SearchFunction buildFunction(final List<ExpressionTreeNode> functionExpressionList) {
        CheckUtils.sizeBetween(functionExpressionList, SEARCH_FUNCTION_INFO.getMinArgsNumber(), SEARCH_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of Search function."));
        return SearchFunction.from(functionExpressionList);
    }

    public enum SearchScope {
        // 自定义空间
        CUSTOMIZESPACE,
        // 请求信息
        REQUESTINFO,
        // 本地缓存
        LOCALCACHE,
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

    /**
     * 搜索函数
     * #search(搜索词)
     * #search(搜索词,搜索范围)
     * #search(搜索词,搜索范围,默认值)
     */
    public static class SearchFunction extends AbstractFunctionExpression {

        private SearchFunction(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        private static SearchFunction from(final List<ExpressionTreeNode> functionExpressionList) {
            return new SearchFunction(functionExpressionList);
        }

        @Override
        public Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            if (childNodeValueList.isEmpty() || childNodeValueList.size() < this.getNodeCount()) {
                throw new DynamicMockException("Parameter incorrectly of `search` function. expect: 1 or 2, actual: " + childNodeValueList.size());
            }

            final Object jsonObject;
            final Object defaultValue = childNodeValueList.size() >= 3 ? childNodeValueList.get(2) : null;

            if (childNodeValueList.size() > 1) {
                final SearchScope searchScope = SearchScope.valueOfName(String.valueOf(childNodeValueList.get(1)));
                if (SearchScope.CUSTOMIZESPACE.equals(searchScope)) {

                    jsonObject = mockHandlerContext.getRequestContext().getCustomizeSpace();
                } else if (SearchScope.REQUESTINFO.equals(searchScope)) {

                    jsonObject = mockHandlerContext.getRequestContext().getRequestInfo();
                } else if (SearchScope.LOCALCACHE.equals(searchScope)) {

                    jsonObject = mockHandlerContext.getRequestContext().getLocalCache();
                } else {

                    jsonObject = childNodeValueList.get(1);
                }
            } else {
                jsonObject = mockHandlerContext.getRequestContext().getCustomizeSpace();
            }

            try {
                return ConvertUtils.getNoNullOrDefault(JsonUtils.jsonPathRead(jsonObject, String.valueOf(childNodeValueList.get(0))), defaultValue);
            } catch (Exception e) {
                log.error("Json path error.", e);
                return defaultValue;
            }
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}