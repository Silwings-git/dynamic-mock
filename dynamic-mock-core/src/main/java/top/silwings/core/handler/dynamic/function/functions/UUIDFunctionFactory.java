package top.silwings.core.handler.dynamic.function.functions;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.function.AbstractDynamicValue;
import top.silwings.core.handler.dynamic.function.FunctionFactory;

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
    public DynamicValue buildFunction(final DynamicValue param) {
        return UUIDFunction.from(param);
    }

    public static class UUIDFunction extends AbstractDynamicValue {

        public UUIDFunction(final DynamicValue param) {
            super(param);
        }

        public static UUIDFunction from(final DynamicValue param) {
            return new UUIDFunction(param);
        }

        @Override
        public Object interpret(final Context context) {

            System.out.println("函数uuid打印结果: " + JSON.toJSONString(this.getParams(context)));
            // TODO_Silwings: 2022/11/7 后续支持参数
            return UUID.randomUUID().toString();
        }
    }

}