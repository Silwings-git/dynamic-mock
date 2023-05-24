package top.silwings.core.handler.plugin.script.mapping;

import top.silwings.core.exceptions.ScriptException;
import top.silwings.core.handler.plugin.script.ScriptLanguageType;

/**
 * @ClassName LanguageScriptMapperFactory
 * @Description 脚本映射接口
 * @Author Silwings
 * @Date 2023/5/16 23:46
 * @Since
 **/
public interface ScriptMapper {

    ScriptLanguageType language();

    <T> T mapping(Class<T> interfaceClass, String scriptText) throws ScriptException;

}