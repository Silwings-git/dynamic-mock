package cn.silwings.admin.plugin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import cn.silwings.core.handler.context.MockPluginContext;
import cn.silwings.core.handler.plugin.MockHandlerPluginInfo;
import cn.silwings.core.handler.plugin.MockPluginInfo;
import cn.silwings.core.handler.plugin.PluginInterfaceType;
import cn.silwings.core.handler.plugin.PluginMetaData;
import cn.silwings.core.handler.plugin.PluginParamFieldType;
import cn.silwings.core.handler.plugin.PluginParamRule;
import cn.silwings.core.handler.plugin.PluginRegistrationProgram;
import cn.silwings.core.handler.plugin.executors.PluginExecutor;
import cn.silwings.core.model.MockHandlerDto;
import cn.silwings.core.utils.JsonUtils;

import java.util.ArrayList;

@Slf4j
@Component
public class LoggerPlugin implements PluginRegistrationProgram {

    @Override
    public PluginExecutor<?> newPluginExecutor(final MockHandlerPluginInfo mockHandlerPluginInfo, final MockHandlerDto definition) {
        return new PluginExecutor<Void>() {

            @Override
            public PluginInterfaceType getPluginInterfaceType() {
                return PluginInterfaceType.PRE_RESPONSE;
            }

            @Override
            public Void execute(final MockPluginContext mockPluginContext) {
                String prefix = String.valueOf(mockHandlerPluginInfo.getPluginParam().get("prefix"));
                if (null == prefix) {
                    prefix = "LoggerPlugin";
                }
                final String contextJson = JsonUtils.toJSONString(mockPluginContext);
                log.info("[{}]-请求响应信息: {}", prefix, contextJson);
                return null;
            }
        };
    }

    @Override
    public MockPluginInfo getMockPluginInfo() {
        final ArrayList<PluginParamRule> pluginParamRuleList = new ArrayList<>();
        pluginParamRuleList.add(PluginParamRule.builder()
                .fieldName("prefix")
                .fieldType(PluginParamFieldType.STRING)
                .required(false)
                .description("输出的日志的前缀")
                .example("一个演示前缀")
                .build());

        return MockPluginInfo.builder()
                .pluginCode("loggerPlugin")
                .pluginName("LoggerPlugin")
                .description("Mock响应日志插件")
                .version("0.0.1")
                .metadata(PluginMetaData.builder().pluginParamRuleList(pluginParamRuleList).build())
                .build();
    }
}
