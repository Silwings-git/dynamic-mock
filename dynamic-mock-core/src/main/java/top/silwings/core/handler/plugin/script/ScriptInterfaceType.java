package top.silwings.core.handler.plugin.script;

import lombok.Getter;
import top.silwings.core.handler.plugin.interceptor.PreMockInterceptor;
import top.silwings.core.handler.plugin.interceptor.PreResponseInterceptor;

import java.lang.reflect.Method;

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
        if (!this.isFunctionalInterface(interceptorClass)) {
            throw new IllegalArgumentException();
        }
        this.typeName = typeName;
        this.interceptorClass = interceptorClass;
    }

    private boolean isFunctionalInterface(final Class<?> functionalInterfaceClass) {
        // 检查是否有 @FunctionalInterface 注解
        if (functionalInterfaceClass.isAnnotationPresent(FunctionalInterface.class)) {
            // 检查抽象方法的数量
            Method[] methods = functionalInterfaceClass.getDeclaredMethods();
            return methods.length == 1;
        }
        return false;
    }

}