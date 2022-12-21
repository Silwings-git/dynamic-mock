package top.silwings.core.handler.tree.dynamic;

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

    DynamicValue buildDynamicValue(List<DynamicValue> dynamicValueList);

}