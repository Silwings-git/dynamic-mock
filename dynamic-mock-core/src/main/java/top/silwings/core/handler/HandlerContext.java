package top.silwings.core.handler;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ParameterContext
 * @Description
 * @Author Silwings
 * @Date 2022/10/29 18:44
 * @Since
 **/
@Getter
@Setter
public class HandlerContext {

    /**
     * 自定义空间
     */
    private Map<?, ?> customizeSpace;


    private final Map<String, Object> parameterMap;

    public HandlerContext() {
        this.parameterMap = new HashMap<>();
    }

    public void putParameter(final String parameterName, final Object parameterValue) {
        this.parameterMap.put(parameterName, parameterValue);
    }

    public Object searchParameter(final String parameterName) {
        return this.parameterMap.get(parameterName);
    }

}