package top.silwings.core.interpreter.expression.function;

import top.silwings.core.interpreter.Expression;
import top.silwings.core.interpreter.expression.SupportAble;

import java.util.List;

/**
 * @ClassName FunctionFactory
 * @Description 方法接口
 * @Author Silwings
 * @Date 2022/11/12 0:03
 * @Since
 **/
public interface FunctionFactory extends SupportAble {

    /**
     * 获取函数描述信息
     */
    FunctionInfo getFunctionInfo();

    /**
     * 创建函数实例
     *
     * @param expressionList 函数参数(尚未被解析)
     * @return 函数实例
     */
    FunctionExpression buildFunction(List<Expression> expressionList);

}