package top.silwings.core.handler.dynamic.operation;

import lombok.Getter;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.operation.extendeds.AdditionExtOperation;
import top.silwings.core.handler.dynamic.operation.extendeds.AndDoubleExtOperation;
import top.silwings.core.handler.dynamic.operation.extendeds.ArithmeticEqualExtOperation;
import top.silwings.core.handler.dynamic.operation.extendeds.ArithmeticNoEqualExtOperation;
import top.silwings.core.handler.dynamic.operation.extendeds.DivisionExtOperation;
import top.silwings.core.handler.dynamic.operation.extendeds.GreaterEqualExtOperation;
import top.silwings.core.handler.dynamic.operation.extendeds.GreaterExtOperation;
import top.silwings.core.handler.dynamic.operation.extendeds.LessExtOperation;
import top.silwings.core.handler.dynamic.operation.extendeds.MultiplicationExtOperation;
import top.silwings.core.handler.dynamic.operation.extendeds.OrDoubleExtOperation;
import top.silwings.core.handler.dynamic.operation.extendeds.RemainderExtOperation;
import top.silwings.core.handler.dynamic.operation.extendeds.SubtractionExtOperation;
import top.silwings.core.exceptions.ExpressionException;

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
public enum ExtendedOperation implements PriorityAble {
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
    LESS_EQUAL("<=", 20, LessExtOperation::new),
    AND_DOUBLE("&&", 10, AndDoubleExtOperation::new),
    OR_DOUBLE("||", 10, OrDoubleExtOperation::new),
    ;

    public static final int LOGICAL_OPERATOR_PRIORITY = 10;
    public static final int COMPARISON_OPERATOR_PRIORITY = 20;

    @Getter
    private final String symbol;

    private final int priority;

    @Getter
    private final Function<List<DynamicValue>, Operator> operatorConstructor;

    ExtendedOperation(final String symbol, final int priority, final Function<List<DynamicValue>, Operator> operatorConstructor) {
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

    @Override
    public int getPriority() {
        return this.priority;
    }
}
