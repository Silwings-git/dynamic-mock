package top.silwings.admin.web.vo.converter;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import top.silwings.admin.web.vo.param.MockHandlerPluginInfoParam;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.handler.plugin.MockHandlerPluginInfo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerTaskVoConverter
 * @Description
 * @Author Silwings
 * @Date 2023/8/13 13:06
 * @Since
 **/
@Component
public class MockHandlerPluginVoConverter {

    public MockHandlerPluginInfo convert(final MockHandlerPluginInfoParam pluginParam) {
        return MockHandlerPluginInfo.builder()
                .pluginCode(pluginParam.getPluginCode())
                .enableStatus(EnableStatus.valueOfCode(pluginParam.getEnableStatus()))
                .pluginParam(pluginParam.getPluginParam())
                .build();
    }

    public MockHandlerPluginInfoParam convert(final MockHandlerPluginInfo pluginDto) {
        final MockHandlerPluginInfoParam param = new MockHandlerPluginInfoParam();
        param.setPluginCode(pluginDto.getPluginCode());
        param.setEnableStatus(param.getEnableStatus());
        param.setPluginParam(pluginDto.getPluginParam());
        return param;
    }

    public List<MockHandlerPluginInfo> convert(final List<MockHandlerPluginInfoParam> pluginInfoList) {
        if (CollectionUtils.isEmpty(pluginInfoList)) {
            return Collections.emptyList();
        }
        return pluginInfoList.stream().map(this::convert).collect(Collectors.toList());
    }

    public List<MockHandlerPluginInfoParam> convert2Param(final List<MockHandlerPluginInfo> pluginInfoList) {
        if (CollectionUtils.isEmpty(pluginInfoList)) {
            return Collections.emptyList();
        }
        return pluginInfoList.stream().map(this::convert).collect(Collectors.toList());
    }
}