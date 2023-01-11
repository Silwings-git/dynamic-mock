package top.silwings.core.interpreter;

import java.util.List;

/**
 * @ClassName TreeNode
 * @Description
 * @Author Silwings
 * @Date 2023/1/11 17:29
 * @Since
 **/
public interface TreeNode<T> {

    List<T> getChildNodes();

}