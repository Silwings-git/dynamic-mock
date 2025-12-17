package cn.silwings.core.handler.plugin;

import lombok.Getter;
import cn.silwings.core.handler.plugin.interfaces.PreMockInterceptor;
import cn.silwings.core.handler.plugin.interfaces.PreResponseInterceptor;

/**
 * @ClassName PluginInterface
 * @Description 插件接口枚举
 * @Author Silwings
 * @Date 2023/5/29 19:36
 * @Since
 **/
@Getter
public enum PluginInterfaceType {

    PRE_MOCK(PreMockInterceptor.class),
    PRE_RESPONSE(PreResponseInterceptor.class),
    ;

    private final Class<?> interfaceClass;

    PluginInterfaceType(final Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }
}
