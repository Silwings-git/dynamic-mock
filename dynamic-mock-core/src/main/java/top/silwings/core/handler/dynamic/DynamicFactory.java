package top.silwings.core.handler.dynamic;

import java.util.List;

/**
 * @ClassName FunctionDynamicValue
 * @Description 函数接口
 * @Author Silwings
 * @Date 2022/11/7 22:27
 * @Since
 **/
public interface DynamicFactory {

    boolean support(String methodName);

    DynamicValue buildFunction(List<DynamicValue> dynamicValueList);

}