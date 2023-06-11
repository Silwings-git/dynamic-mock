package top.silwings.core.handler.plugin.executors.js;

import lombok.extern.slf4j.Slf4j;
import top.silwings.core.handler.plugin.executors.PluginExecutor;
import top.silwings.core.handler.plugin.interfaces.Ordered;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import java.io.IOException;

/**
 * @ClassName AbstractNashornJSScriptExecutor
 * @Description 抽象JS执行器
 * @Author Silwings
 * @Date 2023/6/11 19:45
 * @Since
 **/
@Slf4j
public abstract class AbstractNashornJSScriptExecutor<T> implements PluginExecutor<T> {

    @Override
    public int getOrder() {
        try {
            final Object orderInt = this.getInvocable().invokeFunction(Ordered.class.getDeclaredMethods()[0].getName());
            if (orderInt instanceof Integer) {
                return (int) orderInt;
            }
        } catch (Exception e) {
            log.info(this.scriptName() + " without a getOrder() method, the default weight ordering will be used.");
        }
        return 0;
    }

    @Override
    public void close() throws IOException {
        final ScriptEngine scriptEngine = this.getScriptEngine();
        if (null == scriptEngine) {
            return;
        }
        if (scriptEngine instanceof AutoCloseable) {
            try {
                ((AutoCloseable) scriptEngine).close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    abstract ScriptEngine getScriptEngine();

    abstract Invocable getInvocable();

    abstract String scriptName();

    @Slf4j
    public static class SimpleConsole {

        public void log(final Object obj) {
            if (null == obj) {
                log.info("");
            }
            log.info(String.valueOf(obj));
        }

        public void error(final Object obj) {
            if (null == obj) {
                log.error("");
            }
            log.error(String.valueOf(obj));
        }

        public void warn(final Object obj) {
            if (null == obj) {
                log.warn("");
            }
            log.warn(String.valueOf(obj));
        }

        public void info(final Object obj) {
            if (null == obj) {
                log.info("");
            }
            log.info(String.valueOf(obj));
        }
    }

}