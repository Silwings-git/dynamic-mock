package top.silwings.core.interpreter;

import top.silwings.core.handler.MockHandlerContext;

import java.util.List;

/**
 * @ClassName Node
 * @Description
 * @Author Silwings
 * @Date 2022/10/28 14:54
 * @Since
 **/
public interface Expression extends TreeNode<Expression> {

    Object interpret(MockHandlerContext mockHandlerContext, List<Object> childNodeValueList);

    int getNodeCount();
}