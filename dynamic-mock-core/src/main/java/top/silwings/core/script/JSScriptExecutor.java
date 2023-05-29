package top.silwings.core.script;

/**
 * @ClassName JavaScriptScriptExecutor
 * @Description JS脚本执行器
 * @Author Silwings
 * @Date 2023/5/29 19:45
 * @Since
 **/
public interface JSScriptExecutor<T, E> extends ScriptExecutor<T, E> {

    @Override
    default ScriptLanguage getLanguege() {
        return ScriptLanguage.JAVA_SCRIPT;
    }

}