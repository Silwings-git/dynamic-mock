package top.silwings.core.handler.tree.dynamic.function.functions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
 * @ClassName PrintFunctionFactory
 * @Description 打印函数
 * @Author Silwings
 * @Date 2022/12/31 17:01
 * @Since
 **/
@Slf4j
@Component
public class PrintFunctionFactory implements FunctionFactory {

    private static final FunctionInfo PRINT_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("Print")
            .minArgsNumber(1)
            .maxArgsNumber(1)
            .functionReturnType(FunctionReturnType.OBJECT)
            .build();

    private static final String SYMBOL = "#print(...)";

    @Override
    public boolean support(final String methodName) {
        return "print".equalsIgnoreCase(methodName);
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return PRINT_FUNCTION_INFO;
    }

    @Override
    public PrintFunction buildFunction(final List<DynamicValue> dynamicValueList) {
        CheckUtils.sizeBetween(dynamicValueList, PRINT_FUNCTION_INFO.getMinArgsNumber(), PRINT_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of Print function."));
        return PrintFunction.from(dynamicValueList);
    }

    public static class PrintFunction extends AbstractDynamicValue {

        protected PrintFunction(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        public static PrintFunction from(final List<DynamicValue> dynamicValueList) {
            return new PrintFunction(dynamicValueList);
        }

        @Override
        protected Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            log.info("Print Function :{}", childNodeValueList.get(0));

            return childNodeValueList.get(0);
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}