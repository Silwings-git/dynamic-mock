package top.silwings.core.handler.tree.dynamic.function;

import top.silwings.core.handler.tree.dynamic.DynamicFactory;
import top.silwings.core.handler.tree.dynamic.DynamicValue;

import java.util.List;

/**
 * @ClassName FunctionFactory
 * @Description 方法接口
 * @Author Silwings
 * @Date 2022/11/12 0:03
 * @Since
 **/
public interface FunctionFactory extends DynamicFactory {

    /**
     * 获取函数描述信息
     */
    FunctionInfo getFunctionInfo();

    /**
     * 创建函数实例
     * @param dynamicValueList 函数参数(尚未被解析)
     * @return 函数实例
     */
    DynamicValue buildFunction(List<DynamicValue> dynamicValueList);

    default DynamicValue buildDynamicValue(final List<DynamicValue> dynamicValueList) {
        return this.buildFunction(dynamicValueList);
    }
}