package top.silwings.core.script;

import lombok.Getter;

/**
 * @ClassName ScriptLanguageType
 * @Description 脚本语言类型
 * @Author Silwings
 * @Date 2023/5/16 22:42
 * @Since
 **/
@Getter
public enum ScriptLanguage {

    JAVA("java"),
    JAVA_SCRIPT("javaScript"),
    ;

    private final String language;

    ScriptLanguage(final String language) {
        this.language = language;
    }
}