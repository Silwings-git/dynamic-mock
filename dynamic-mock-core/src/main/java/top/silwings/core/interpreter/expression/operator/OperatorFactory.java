package top.silwings.core.interpreter.expression.operator;

import top.silwings.core.interpreter.Expression;
import top.silwings.core.interpreter.expression.SupportAble;

import java.util.List;

/**
 * @ClassName OperatorFactory
 * @Description
 * @Author Silwings
 * @Date 2022/11/11 23:07
 * @Since
 **/
public interface OperatorFactory extends SupportAble, PriorityAble {

    String getOperatorSymbol();

    OperatorExpression buildOperator(List<Expression> expressionList);

}