package top.silwings.core.interpreter.dynamic_expression.function;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

/**
 * @ClassName FunctionInfo
 * @Description 函数信息
 * @Author Silwings
 * @Date 2022/11/25 20:43
 * @Since
 **/
@Getter
@Builder
public class FunctionInfo implements Comparable<FunctionInfo> {

    /**
     * 函数名
     */
    private final String functionName;

    /**
     * 最小参数数量
     */
    private final int minArgsNumber;

    /**
     * 最大参数数量
     */
    private final int maxArgsNumber;

    private final FunctionReturnType functionReturnType;

    @Override
    public int compareTo(final FunctionInfo other) {
        return this.functionName.compareTo(other.getFunctionName());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FunctionInfo that = (FunctionInfo) o;

        return Objects.equals(this.functionName, that.functionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.functionName);
    }
}