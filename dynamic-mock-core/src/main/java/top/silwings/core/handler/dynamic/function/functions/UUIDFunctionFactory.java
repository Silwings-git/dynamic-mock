package top.silwings.core.handler.dynamic.function.functions;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicDataException;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.function.FunctionFactory;

import java.util.List;
import java.util.UUID;

/**
 * @ClassName UUIDFunctionFactory
 * @Description UUID
 * @Author Silwings
 * @Date 2022/11/8 22:30
 * @Since
 **/
@Component
public class UUIDFunctionFactory implements FunctionFactory {
    @Override
    public boolean support(final String methodName) {
        return "uuid".equalsIgnoreCase(methodName);
    }

    @Override
    public DynamicValue buildFunction(final List<DynamicValue> dynamicValueList) {
        return UUIDFunction.from(dynamicValueList);
    }

    public static class UUIDFunction extends AbstractDynamicValue {

        public UUIDFunction(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        public static UUIDFunction from(final List<DynamicValue> dynamicValueList) {
            return new UUIDFunction(dynamicValueList);
        }

        @Override
        public Object interpret(final Context context, final List<Object> childNodeValueList) {
            if (this.getNodeCount() > 0 && CollectionUtils.isEmpty(childNodeValueList) && childNodeValueList.size() < this.getNodeCount()) {
                throw new DynamicDataException("缺少参数");
            }
            return UUID.randomUUID().toString();
        }

        @Override
        public int getNodeCount() {
            return this.getChildNodes().size();
        }

    }

}