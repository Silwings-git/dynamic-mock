package top.silwings.core.handler.dynamic.function.operations;

import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.function.AbstractFunctionDynamicValue;

/**
 * @ClassName ArithmeticNoEqualExtOperation
 * @Description 数值不等
 * @Author Silwings
 * @Date 2022/11/7 21:30
 * @Since
 **/
public class ArithmeticNoEqualExtOperation extends AbstractFunctionDynamicValue implements DynamicValue {

    private final ArithmeticEqualExtOperation equalExtOperation;

    public ArithmeticNoEqualExtOperation(final DynamicValue param) {
        super(param);
        this.equalExtOperation = new ArithmeticEqualExtOperation(param);
    }

    @Override
    public Boolean interpret(final Context parameterContext) {
        return !this.equalExtOperation.interpret(parameterContext);
    }

}