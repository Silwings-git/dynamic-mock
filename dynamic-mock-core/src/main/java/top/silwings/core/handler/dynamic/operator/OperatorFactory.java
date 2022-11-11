package top.silwings.core.handler.dynamic.operator;

import top.silwings.core.handler.dynamic.DynamicFactory;

/**
 * @ClassName OperatorFactory
 * @Description
 * @Author Silwings
 * @Date 2022/11/11 23:07
 * @Since
 **/
public interface OperatorFactory extends DynamicFactory, PriorityAble {

    String getOperatorSymbol();

}