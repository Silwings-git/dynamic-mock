package top.silwings.core.handler.tree;

import top.silwings.core.handler.Context;

import java.util.List;

/**
 * @ClassName Node
 * @Description
 * @Author Silwings
 * @Date 2022/10/28 14:54
 * @Since
 **/
public interface Node {

    Object interpret(Context context, List<Object> childNodeValueList);

    List<Node> getChildNodes();

    int getNodeCount();
}