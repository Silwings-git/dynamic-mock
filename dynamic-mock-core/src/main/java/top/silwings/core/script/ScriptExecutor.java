package top.silwings.core.script;

/**
 * @ClassName ScriptExecutor
 * @Description 脚本执行器
 * @Author Silwings
 * @Date 2023/5/29 19:42
 * @Since
 **/
public interface ScriptExecutor<T, E> {
    ScriptLanguage getLanguege();

    T execute(E e);
}