package top.silwings.core.interpreter;

import top.silwings.core.handler.context.MockHandlerContext;

import java.util.List;

/**
 * @ClassName Expression
 * @Description 表达式
 * @Author Silwings
 * @Date 2023/1/11 21:48
 * @Since
 **/
public interface Expression {

    Object interpret(MockHandlerContext mockHandlerContext, List<Object> childNodeValueList);

}