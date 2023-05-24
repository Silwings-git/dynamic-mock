package top.silwings.core.handler.plugin.script.mapping;

import org.python.util.PythonInterpreter;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.ScriptException;
import top.silwings.core.handler.plugin.script.ScriptLanguageType;

import java.lang.reflect.Method;

/**
 * @ClassName PythonScriptMappingFactory
 * @Description python脚本映射工厂
 * @Author Silwings
 * @Date 2023/5/16 23:47
 * @Since
 **/
@Component
public class PythonScriptMappingFactory implements ScriptMapper {

    @Override
    public ScriptLanguageType language() {
        return ScriptLanguageType.PYTHON;
    }

    @Override
    public <T> T mapping(final Class<T> interfaceClass, final String scriptText) throws ScriptException{
        try (final PythonInterpreter interpreter = new PythonInterpreter()) {

            // 执行 Python 脚本
            interpreter.exec(scriptText);

            // ScriptInterfaceType可以保证拿到的class一定是函数式接口
            final Method method = interfaceClass.getDeclaredMethods()[0];

            final T script = interpreter.get(method.getName(), interfaceClass);
            if (null == script) {
                throw new ScriptException();
            }
            return script;
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

}