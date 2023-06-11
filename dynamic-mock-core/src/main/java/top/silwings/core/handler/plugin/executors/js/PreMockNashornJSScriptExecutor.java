package top.silwings.core.handler.plugin.executors.js;

import top.silwings.core.exceptions.ScriptException;
import top.silwings.core.handler.context.MockPluginContext;
import top.silwings.core.handler.plugin.PluginInterfaceType;
import top.silwings.core.handler.plugin.executors.PluginExecutor;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * @ClassName PreMockJavaScriptScriptExecutor
 * @Description PreMock的js脚本执行器
 * @Author Silwings
 * @Date 2023/5/29 19:52
 * @Since
 **/
public class PreMockNashornJSScriptExecutor extends AbstractNashornJSScriptExecutor<Void> implements PluginExecutor<Void> {

    private final ScriptEngine engine;
    private final Invocable invocable;

    private PreMockNashornJSScriptExecutor(final String script) throws javax.script.ScriptException {
        // 使用jdk11以上版本无法获取到engine
        this.engine = new ScriptEngineManager().getEngineByName("nashorn");
        // 实现一个简单的console实例用于打印信息
        this.engine.put("console", new SimpleConsole());
        this.engine.eval(script);
        this.invocable = (Invocable) this.engine;
    }

    public static PreMockNashornJSScriptExecutor from(final String script) {
        try {
            return new PreMockNashornJSScriptExecutor(script);
        } catch (javax.script.ScriptException e) {
            throw new ScriptException(e);
        }
    }

    @Override
    public PluginInterfaceType getPluginInterfaceType() {
        return PluginInterfaceType.PRE_MOCK;
    }

    @Override
    public Void execute(final MockPluginContext mockPluginContext) {
        try {
            this.invocable.invokeFunction(this.getPluginInterfaceType().getInterfaceClass().getDeclaredMethods()[0].getName(), mockPluginContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    ScriptEngine getScriptEngine() {
        return this.engine;
    }

    @Override
    Invocable getInvocable() {
        return this.invocable;
    }
}