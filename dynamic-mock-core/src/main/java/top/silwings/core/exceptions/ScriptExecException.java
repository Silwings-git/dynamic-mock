package top.silwings.core.exceptions;

/**
 * @ClassName ScriptExecException
 * @Description 脚本执行异常
 * @Author Silwings
 * @Date 2023/5/16 23:49
 * @Since
 **/
public class ScriptExecException extends BaseDynamicMockException {
    public ScriptExecException(final Throwable cause) {
        super("Script execution exception: " + cause.getMessage(), cause);
    }
}