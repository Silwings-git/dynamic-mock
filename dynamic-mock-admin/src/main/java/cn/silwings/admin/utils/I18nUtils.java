package cn.silwings.admin.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName I18nUtils
 * @Description 国际化工具类
 * @Author Silwings
 * @Date 2022/11/22 22:35
 * @Since
 **/
@Slf4j
public class I18nUtils {

    private static final Map<String, Properties> propertiesMap = new ConcurrentHashMap<>();

    private I18nUtils() {
        throw new AssertionError();
    }

    public static Properties loadI18nProp(final String language) {

        if (propertiesMap.containsKey(language)) {
            return propertiesMap.get(language);
        }

        try {
            final String i18nFile = MessageFormat.format("i18n/language_{0}.properties", language);

            final Resource resource = new ClassPathResource(i18nFile);
            final EncodedResource encodedResource = new EncodedResource(resource, StandardCharsets.UTF_8);
            final Properties prop = PropertiesLoaderUtils.loadProperties(encodedResource);
            propertiesMap.put(language, prop);

        } catch (IOException e) {
            log.error("加载国际化文件失败. 参数信息: {}", language);
            log.error(e.getMessage(), e);
            propertiesMap.put(language, new Properties());
        }

        return propertiesMap.get(language);
    }

    /**
     * 获取key对应的值
     */
    public static String getValue(final String language, final String key, final Object[] argArray) {
        final String messagePattern = loadI18nProp(language).getProperty(key);
        if (StringUtils.isBlank(messagePattern)) {
            return messagePattern;
        }
        return MessageFormat.format(messagePattern, argArray);
    }

}
