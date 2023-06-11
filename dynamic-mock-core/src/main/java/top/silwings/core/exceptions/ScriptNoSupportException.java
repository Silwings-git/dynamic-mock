package top.silwings.core.exceptions;

/**
 * @ClassName ScriptNoSupportException
 * @Description 脚本不支持异常
 * @Author Silwings
 * @Date 2023/6/11 19:35
 * @Since
 **/
public class ScriptNoSupportException extends BaseDynamicMockException {
    public ScriptNoSupportException(final String message) {
        super(message);
    }
}