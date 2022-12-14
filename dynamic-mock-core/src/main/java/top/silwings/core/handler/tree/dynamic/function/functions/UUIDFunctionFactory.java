package top.silwings.core.handler.tree.dynamic.function.functions;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.handler.tree.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.function.FunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.FunctionInfo;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.TypeUtils;

import java.util.List;
import java.util.UUID;

/**
 * @ClassName UUIDFunctionFactory
 * @Description UUID
 * @Author Silwings
 * @Date 2022/11/8 22:30
 * @Since
 **/
@Slf4j
@Component
public class UUIDFunctionFactory implements FunctionFactory {

    private static final FunctionInfo UUID_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("UUID")
            .minArgsNumber(0)
            .maxArgsNumber(3)
            .build();
    private static final String SYMBOL = "#uuid(...)";

    @Override
    public FunctionInfo getFunctionInfo() {
        return UUID_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "uuid".equalsIgnoreCase(methodName);
    }

    @Override
    public DynamicValue buildFunction(final List<DynamicValue> dynamicValueList) {
        return UUIDFunction.from(dynamicValueList);
    }

    /**
     * uuid函数
     * #uuid()
     * #uuid(prefix,length,replace)
     * 三个参数全部选填,但填后面的参数时,前面的参数必须全部填写,示例: #uuid(,18),#uuid(,,true)
     * prefix: 字符类型, 生成的id以什么作为前缀
     * length: 数值类型, 生成的id的长度(不包含prefix),有最大值限制,如果replace为true,仅支持32,否则支持36
     * replace: 布尔类型, 是否将'-'替换为''
     * 如果参数不合法不会抛出异常,而是忽略该参数
     */
    public static class UUIDFunction extends AbstractDynamicValue {

        private UUIDFunction(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        public static UUIDFunction from(final List<DynamicValue> dynamicValueList) {
            CheckUtils.sizeBetween(dynamicValueList, UUID_FUNCTION_INFO.getMinArgsNumber(), UUID_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of UUID function."));
            return new UUIDFunction(dynamicValueList);
        }

        @Override
        public String doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            if (this.getNodeCount() > 0 && childNodeValueList.size() != this.getNodeCount()) {
                throw new DynamicMockException("Parameter incorrectly of `uuid` function");
            }

            // 前缀
            String prefix = "";
            if (!childNodeValueList.isEmpty()
                    && null != childNodeValueList.get(0)
                    && StringUtils.isNotBlank(String.valueOf(childNodeValueList.get(0)))) {

                prefix = String.valueOf(childNodeValueList.get(0));
            }

            // 是否替换
            boolean replace = false;
            if (childNodeValueList.size() >= 3 && null != childNodeValueList.get(2)) {

                try {
                    replace = TypeUtils.toBooleanValue(childNodeValueList.get(2));
                } catch (Exception e) {
                    log.error("The parameter of `uuid` function cannot be converted to boolean value.");
                }
            }

            // 长度
            int length = replace ? 32 : 36;
            if (childNodeValueList.size() >= 2
                    && null != childNodeValueList.get(1)
                    && StringUtils.isNotBlank(String.valueOf(childNodeValueList.get(1)))) {

                try {
                    length = TypeUtils.toBigDecimal(childNodeValueList.get(1)).intValue();
                } catch (Exception e) {
                    log.error("The parameter of `uuid` function cannot be converted to a numeric value.");
                }
            }

            return this.generateUUID(prefix, replace, length);
        }

        private String generateUUID(final String prefix, final boolean replace, final int length) {

            final StringBuilder builder = new StringBuilder(prefix);

            String uuid = UUID.randomUUID().toString();
            if (replace) {
                uuid = uuid.replace("-", "");
            }

            if (uuid.length() > length) {

                builder.append(uuid, 0, length);

            } else {

                final int diff = length - uuid.length();
                final int m = diff % uuid.length();

                for (int i = 0; i < length / uuid.length(); i++) {
                    builder.append(uuid);
                }

                builder.append(uuid, 0, m);
            }

            return builder.toString();
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }

    }

}