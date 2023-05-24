package top.silwings.core.exceptions;

/**
 * @ClassName ScriptException
 * @Description 脚本错误
 * @Author Silwings
 * @Date 2023/5/16 23:49
 * @Since
 **/
public class ScriptException extends BaseDynamicMockException {
    public ScriptException() {
        super("Script does not conform to spec !");
    }

    public ScriptException(final Throwable throwable) {
        super("Script does not conform to spec: " + throwable.getMessage(), throwable);
    }
}