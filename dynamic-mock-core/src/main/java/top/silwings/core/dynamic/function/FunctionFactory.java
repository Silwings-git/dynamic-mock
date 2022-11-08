package top.silwings.core.dynamic.function;

import top.silwings.core.dynamic.DynamicValue;

/**
 * @ClassName FunctionDynamicValue
 * @Description 函数接口
 * @Author Silwings
 * @Date 2022/11/7 22:27
 * @Since
 **/
public interface FunctionFactory {

    boolean support(String methodName);

    FunctionDynamicValue buildFunction(DynamicValue param);

}