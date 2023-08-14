package top.silwings.core.exceptions;

/**
 * @ClassName NoMatchingPluginRegistrationProgramException
 * @Description
 * @Author Silwings
 * @Date 2023/8/13 13:50
 * @Since
 **/
public class NoMatchingPluginRegistrationProgramException extends BaseDynamicMockException {
    public NoMatchingPluginRegistrationProgramException(final String pluginCode) {
        super("No matching plugin registration program, plugin code: " + pluginCode);
    }
}