package top.silwings.core.handler.plugin.script;

import lombok.Getter;

/**
 * @ClassName ScriptLanguageType
 * @Description 脚本语言类型
 * @Author Silwings
 * @Date 2023/5/16 22:42
 * @Since
 **/
@Getter
public enum ScriptLanguageType {

    PYTHON("python"),
    ;

    private final String language;

    ScriptLanguageType(final String language) {
        this.language = language;
    }
}