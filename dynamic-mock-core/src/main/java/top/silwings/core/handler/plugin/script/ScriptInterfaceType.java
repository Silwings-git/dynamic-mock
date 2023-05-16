package top.silwings.core.handler.plugin.script;

import lombok.Getter;
import top.silwings.core.handler.plugin.interceptor.PreMockInterceptor;
import top.silwings.core.handler.plugin.interceptor.PreResponseInterceptor;

/**
 * @ClassName ScriptInterfaceType
 * @Description 脚本接口类型
 * @Author Silwings
 * @Date 2023/5/16 22:38
 * @Since
 **/
@Getter
public enum ScriptInterfaceType {

    PRE_MOCK_INTERCEPTOR("PreMockInterceptor", PreMockInterceptor.class),
    PRE_RESPONSE_INTERCEPTOR("PreResponseInterceptor", PreResponseInterceptor.class),
    ;

    private final String typeName;
    private final Class<?> interceptorClass;

    ScriptInterfaceType(final String typeName, final Class<?> interceptorClass) {
        this.typeName = typeName;
        this.interceptorClass = interceptorClass;
    }
}
