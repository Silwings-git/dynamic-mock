package top.silwings.core.script;

/**
 * @ClassName JavaScriptExecutor
 * @Description Java脚本执行器
 * @Author Silwings
 * @Date 2023/5/29 19:44
 * @Since
 **/
public interface JavaScriptExecutor<T, E> extends ScriptExecutor<T, E> {

    @Override
    default ScriptLanguage getLanguege() {
        return ScriptLanguage.JAVA;
    }

}