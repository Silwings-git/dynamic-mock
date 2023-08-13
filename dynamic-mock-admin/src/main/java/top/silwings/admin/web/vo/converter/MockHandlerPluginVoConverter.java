package top.silwings.admin.web.vo.converter;

import org.springframework.stereotype.Component;
import top.silwings.admin.web.vo.param.MockHandlerPluginInfoParam;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.model.MockHandlerPluginInfoDto;

/**
 * @ClassName MockHandlerTaskVoConverter
 * @Description
 * @Author Silwings
 * @Date 2023/8/13 13:06
 * @Since
 **/
@Component
public class MockHandlerPluginVoConverter {

    public MockHandlerPluginInfoDto convert(final MockHandlerPluginInfoParam pluginParam) {
        return MockHandlerPluginInfoDto.builder()
                .pluginCode(pluginParam.getPluginCode())
                .enableStatus(EnableStatus.valueOfCode(pluginParam.getEnableStatus()))
                .pluginParam(pluginParam.getPluginParam())
                .build();
    }

    public MockHandlerPluginInfoParam convert(final MockHandlerPluginInfoDto pluginDto) {
        final MockHandlerPluginInfoParam param = new MockHandlerPluginInfoParam();
        param.setPluginCode(pluginDto.getPluginCode());
        param.setEnableStatus(param.getEnableStatus());
        param.setPluginParam(pluginDto.getPluginParam());
        return param;
    }
}