package top.silwings.core.handler.tree.dynamic.operator;

/**
 * @ClassName OperatorType
 * @Description
 * @Author Silwings
 * @Date 2022/11/11 23:10
 * @Since
 **/
public enum OperatorType implements PriorityAble {

    /**
     * 10 逻辑运算符
     */
    LOGICAL(10),
    /**
     * 20 比较运算符
     */
    COMPARISON(20),
    /**
     * 30 算术运算符一
     */
    ARITHMETIC_ONE(30),
    /**
     * 40 算术运算符二
     */
    ARITHMETIC_TWO(40),
    ;

    private final int priority;

    OperatorType(final int priority) {
        this.priority = priority;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }
}
