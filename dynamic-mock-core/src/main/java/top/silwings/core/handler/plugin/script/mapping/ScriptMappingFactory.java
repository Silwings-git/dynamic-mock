package top.silwings.core.handler.plugin.script.mapping;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import top.silwings.core.handler.plugin.script.ScriptLanguageType;

import java.util.EnumMap;
import java.util.Map;

/**
 * @ClassName Script2JavaInterpreterFactory
 * @Description 脚本转java解释器
 * @Author Silwings
 * @Date 2023/5/16 23:47
 * @Since
 **/
@Component
public class ScriptMappingFactory implements ApplicationContextAware {

    private final Map<ScriptLanguageType, ScriptMapper> scriptMapperMap = new EnumMap<>(ScriptLanguageType.class);

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        final Map<String, ScriptMapper> beansOfType = applicationContext.getBeansOfType(ScriptMapper.class);
        beansOfType.values().forEach(scriptMapper -> this.scriptMapperMap.put(scriptMapper.language(), scriptMapper));
    }

    public <T> T mapping(final ScriptLanguageType scriptLanguageType, final Class<T> interfaceClass, final String scriptText) {
        return this.scriptMapperMap.get(scriptLanguageType).mapping(interfaceClass, scriptText);
    }

}