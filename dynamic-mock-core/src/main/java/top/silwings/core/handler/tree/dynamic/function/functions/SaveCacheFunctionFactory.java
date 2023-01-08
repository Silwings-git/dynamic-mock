package top.silwings.core.handler.tree.dynamic.function.functions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.handler.tree.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.function.FunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.FunctionInfo;
import top.silwings.core.handler.tree.dynamic.function.FunctionReturnType;
import top.silwings.core.utils.CheckUtils;

import java.util.List;

/**
 * @ClassName SaveCacheFunctionFactory
 * @Description 保存缓存
 * @Author Silwings
 * @Date 2023/1/5 21:42
 * @Since
 **/
@Component
public class SaveCacheFunctionFactory implements FunctionFactory {

    private static final FunctionInfo SAVE_CACHE_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("SaveCache")
            .minArgsNumber(2)
            .maxArgsNumber(3)
            .functionReturnType(FunctionReturnType.OBJECT)
            .build();

    private static final String SYMBOL = "#SaveCache(...)";

    @Override
    public boolean support(final String methodName) {
        return "SaveCache".equalsIgnoreCase(methodName);
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return SAVE_CACHE_FUNCTION_INFO;
    }

    @Override
    public SaveCacheFunction buildFunction(final List<DynamicValue> dynamicValueList) {
        CheckUtils.sizeBetween(dynamicValueList, SAVE_CACHE_FUNCTION_INFO.getMinArgsNumber(), SAVE_CACHE_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of SaveCache function."));
        return SaveCacheFunction.from(dynamicValueList);
    }

    public enum ReturnType {

        // 返回key值
        KEY,
        // 返回value
        VALUE,
        // 返回CacheEntry
        ALL,
        ;

        public static ReturnType valueOfName(final String name) {

            if (StringUtils.isBlank(name)) {
                return null;
            }
            for (final SaveCacheFunctionFactory.ReturnType value : values()) {
                if (value.name().equalsIgnoreCase(name)) {
                    return value;
                }
            }

            return null;
        }

    }

    /**
     * 保存数据到缓存,并返回该数据
     * #saveCache(key,value)
     * #saveCache(key,value,returnType)
     */
    public static class SaveCacheFunction extends AbstractDynamicValue {

        public SaveCacheFunction(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        public static SaveCacheFunction from(final List<DynamicValue> dynamicValueList) {
            return new SaveCacheFunction(dynamicValueList);
        }

        @Override
        protected Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            final ReturnType returnType;

            if (childNodeValueList.size() >= 3) {

                final String returnTypeStr = String.valueOf(childNodeValueList.get(2));
                returnType = ReturnType.valueOfName(returnTypeStr);
                CheckUtils.isNotNull(returnType, DynamicMockException.supplier("The return type of the SaveCache function does not match : " + returnTypeStr));
            } else {

                returnType = ReturnType.VALUE;
            }

            final Object key = childNodeValueList.get(0);
            final Object value = childNodeValueList.get(1);

            mockHandlerContext.getRequestContext().getLocalCache().put(key, value);

            if (ReturnType.KEY.equals(returnType)) {

                // 返回key值
                return key;
            } else if (ReturnType.VALUE.equals(returnType)) {

                // 返回value
                return value;
            } else {

                // 返回CacheEntry
                return new CacheEntry(key, value);
            }
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

    @Getter
    @ToString
    @AllArgsConstructor
    public static class CacheEntry {

        private final Object key;

        private final Object value;

    }

}