package top.silwings.core;

/**
 * @ClassName Context
 * @Description 全局信息
 * @Author Silwings
 * @Date 2022/10/28 17:32
 * @Since
 **/
public class Context {

    private final StringBuilder builder;

    private final ParameterContext parameterContext;


    public Context(final int capacity, final ParameterContext parameterContext) {
        this.builder = new StringBuilder(capacity);
        this.parameterContext = parameterContext;
    }

    public Context(final int capacity) {
        this(capacity, new ParameterContext());
    }

    public Context() {
        this(16);
    }

    public StringBuilder getBuilder() {
        return this.builder;
    }

    public String getJsonStr() {
        return this.builder.toString();
    }

    public ParameterContext getParameterContext() {
        return parameterContext;
    }
}