package top.silwings.core.handler.dynamic.function.functions;

import com.alibaba.fastjson.JSON;
import top.silwings.core.handler.ParameterContext;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.function.AbstractFunctionDynamicValue;
import top.silwings.core.handler.dynamic.function.FunctionDynamicValue;
import top.silwings.core.handler.dynamic.function.FunctionFactory;

import java.util.UUID;

/**
 * @ClassName UUIDFunctionFactory
 * @Description UUID
 * @Author Silwings
 * @Date 2022/11/8 22:30
 * @Since
 **/
public class UUIDFunctionFactory implements FunctionFactory {
    @Override
    public boolean support(final String methodName) {
        return "uuid".equalsIgnoreCase(methodName);
    }

    @Override
    public FunctionDynamicValue buildFunction(final DynamicValue param) {
        return UUIDFunction.from(param);
    }

    public static class UUIDFunction extends AbstractFunctionDynamicValue {

        public UUIDFunction(final DynamicValue param) {
            super(param);
        }

        public static UUIDFunction from(final DynamicValue param) {
            return new UUIDFunction(param);
        }

        @Override
        public Object value(final ParameterContext context) {

            System.out.println("函数uuid打印结果: " + JSON.toJSONString(this.getParams(context)));
            // TODO_Silwings: 2022/11/7 后续支持参数
            return UUID.randomUUID().toString();
        }
    }

}