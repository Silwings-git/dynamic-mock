package top.silwings.core.handler.dynamic.operation.extendeds;

import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.DynamicValue;

import java.util.List;

/**
 * @ClassName ArithmeticNoEqualExtOperation
 * @Description 数值不等
 * @Author Silwings
 * @Date 2022/11/7 21:30
 * @Since
 **/
public class ArithmeticNoEqualExtOperation extends AbstractCompareExtOperation {

    private final ArithmeticEqualExtOperation equalExtOperation;

    public ArithmeticNoEqualExtOperation(final List<DynamicValue> paramList) {
        super(paramList);
        this.equalExtOperation = new ArithmeticEqualExtOperation(paramList);
    }

    @Override
    public Boolean interpret(final Context parameterContext) {
        return !this.equalExtOperation.interpret(parameterContext);
    }

}