package top.silwings.core.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ParameterContext
 * @Description
 * @Author Silwings
 * @Date 2022/10/29 18:44
 * @Since
 **/
public class ParameterContext {

    public static final ParameterContext EMPTY_CONTEXT = new ParameterContext();

    private final Map<String, Object> parameterMap;

    public ParameterContext() {
        this.parameterMap = new HashMap<>();
    }

    public void putParameter(final String parameterName, final Object parameterValue) {
        this.parameterMap.put(parameterName, parameterValue);
    }

    public Object searchParameter(final String parameterName) {
        return this.parameterMap.get(parameterName);
    }

    public static ParameterContext emptyContext() {
        return EMPTY_CONTEXT;
    }

}