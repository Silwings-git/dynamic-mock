package top.silwings.core.handler.dynamic.function;

import lombok.Getter;
import top.silwings.core.exceptions.ExpressionException;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.function.operations.AdditionExtOperation;
import top.silwings.core.handler.dynamic.function.operations.AndDoubleExtOperation;
import top.silwings.core.handler.dynamic.function.operations.ArithmeticEqualExtOperation;
import top.silwings.core.handler.dynamic.function.operations.ArithmeticNoEqualExtOperation;
import top.silwings.core.handler.dynamic.function.operations.DivisionExtOperation;
import top.silwings.core.handler.dynamic.function.operations.GreaterEqualExtOperation;
import top.silwings.core.handler.dynamic.function.operations.GreaterExtOperation;
import top.silwings.core.handler.dynamic.function.operations.LessEqualExtOperation;
import top.silwings.core.handler.dynamic.function.operations.LessExtOperation;
import top.silwings.core.handler.dynamic.function.operations.MultiplicationExtOperation;
import top.silwings.core.handler.dynamic.function.operations.OrDoubleExtOperation;
import top.silwings.core.handler.dynamic.function.operations.RemainderExtOperation;
import top.silwings.core.handler.dynamic.function.operations.SubtractionExtOperation;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName ExtendedOperation
 * @Description 操作符
 * @Author Silwings
 * @Date 2022/11/6 14:51
 * @Since
 **/
public enum ExtendedOperation {
    ADDITION("+", 30, AdditionExtOperation::new),
    SUBTRACTION("-", 30, SubtractionExtOperation::new),
    MULTIPLICATION("*", 40, MultiplicationExtOperation::new),
    DIVISION("/", 40, DivisionExtOperation::new),
    REMAINDER("%", 40, RemainderExtOperation::new),
    ARITHMETIC_EQUAL("==", 20, ArithmeticEqualExtOperation::new),
    ARITHMETIC_NO_EQUAL("!=", 20, ArithmeticNoEqualExtOperation::new),
    GREATER(">", 20, GreaterExtOperation::new),
    GREATER_EQUAL(">=", 20, GreaterEqualExtOperation::new),
    LESS("<", 20, LessExtOperation::new),
    LESS_EQUAL("<=", 20, LessEqualExtOperation::new),
    AND_DOUBLE("&&", 10, AndDoubleExtOperation::new),
    OR_DOUBLE("||", 10, OrDoubleExtOperation::new),
    ;

    @Getter
    private final String symbol;

    private final int priority;

    @Getter
    private final Function<DynamicValue, FunctionDynamicValue> operatorConstructor;

    ExtendedOperation(final String symbol, final int priority, final Function<DynamicValue, FunctionDynamicValue> operatorConstructor) {
        this.symbol = symbol;
        this.priority = priority;
        this.operatorConstructor = operatorConstructor;
    }

    public static ExtendedOperation valueOfSymbol(final String symbol) {

        for (final ExtendedOperation value : values()) {
            if (value.symbol.equals(symbol)) {
                return value;
            }
        }

        throw new ExpressionException();
    }

    public static List<String> operatorSymbols() {
        return Arrays.stream(values()).map(ExtendedOperation::getSymbol).collect(Collectors.toList());
    }

    public static boolean isOperation(final String symbol) {
        for (final ExtendedOperation value : values()) {
            if (value.symbol.equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    public static int getPriority(final String symbol) {
        return valueOfSymbol(symbol).getPriority();
    }

    public int getPriority() {
        return this.priority;
    }
}
