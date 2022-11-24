package top.silwings.admin.auth;

import lombok.Builder;
import top.silwings.core.utils.ConvertUtils;

/**
 * @ClassName WebContext
 * @Description web上下文
 * @Author Silwings
 * @Date 2022/11/22 22:49
 * @Since
 **/
@Builder
public class WebContext {

    private static final WebContext EMPTY = WebContext.builder().build();

    private static final String DEFAULT_LANGUAGE = "zh-CN";

    private final String language;

    public String getLanguage() {
        return ConvertUtils.getNoBlankOrDefault(this.language, DEFAULT_LANGUAGE);
    }
}