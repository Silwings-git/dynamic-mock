package top.silwings.core.dynamic;

import top.silwings.core.ParameterContext;

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