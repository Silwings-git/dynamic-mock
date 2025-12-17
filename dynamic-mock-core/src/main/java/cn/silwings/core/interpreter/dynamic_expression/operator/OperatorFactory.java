package cn.silwings.core.interpreter.dynamic_expression.operator;

import cn.silwings.core.interpreter.ExpressionTreeNode;
import cn.silwings.core.interpreter.dynamic_expression.SupportAble;

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

    OperatorExpression buildOperator(List<ExpressionTreeNode> expressionList);

}