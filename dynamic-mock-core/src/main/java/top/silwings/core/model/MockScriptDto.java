package top.silwings.core.model;

import lombok.Builder;
import lombok.Getter;
import top.silwings.core.handler.plugin.PluginInterfaceType;
import top.silwings.core.script.ScriptLanguage;

/**
 * @ClassName MockScriptDto
 * @Description Mock脚本
 * @Author Silwings
 * @Date 2023/5/16 22:34
 * @Since
 **/
@Getter
@Builder
public class MockScriptDto {

    /**
     * 脚本名称
     */
    private String scriptName;

    /**
     * 实现接口类型
     */
    private PluginInterfaceType interfaceType;

    /**
     * 脚本语言类型
     */
    private ScriptLanguage scriptLanguage;

    /**
     * 脚本代码
     */
    private String scriptText;

    /**
     * 脚本备注
     */
    private String remark;

}