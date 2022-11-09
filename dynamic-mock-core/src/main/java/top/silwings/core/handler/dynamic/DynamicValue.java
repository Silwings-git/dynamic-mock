package top.silwings.core.handler.dynamic;

import top.silwings.core.handler.ParameterContext;

/**
 * @ClassName ValueAble
 * @Description
 * @Author Silwings
 * @Date 2022/11/7 20:50
 * @Since
 **/
public interface DynamicValue {

    Object value(final ParameterContext context);

}