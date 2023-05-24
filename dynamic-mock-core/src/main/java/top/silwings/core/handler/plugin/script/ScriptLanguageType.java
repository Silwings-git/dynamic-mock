package top.silwings.core.handler.plugin.script;

import lombok.Getter;
import top.silwings.core.handler.plugin.script.mapping.PythonScriptMappingFactory;
import top.silwings.core.handler.plugin.script.mapping.ScriptMapper;

/**
 * @ClassName ScriptLanguageType
 * @Description 脚本语言类型
 * @Author Silwings
 * @Date 2023/5/16 22:42
 * @Since
 **/
@Getter
public enum ScriptLanguageType {

    PYTHON("python", PythonScriptMappingFactory.class),
    ;

    private final String language;
    private final Class<? extends ScriptMapper> scriptMapperClass;

    ScriptLanguageType(final String language, final Class<? extends ScriptMapper> scriptMapperClass) {
        this.language = language;
        this.scriptMapperClass = scriptMapperClass;
    }
}