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
        // 仅当language是Language枚举支持的才使用language返回
        return ConvertUtils.getNoBlankOrDefault(Language.support(this.language) ? this.language : null, DEFAULT_LANGUAGE);
    }

    enum Language{

        ZH_CN("zh-CN"),
        EN("en"),
        ;

        private final String code;

        Language(final String code) {
            this.code = code;
        }

        public static boolean support(final String code) {
            for (final Language value : values()) {
                if (value.code.equals(code)) {
                    return true;
                }
            }
            return false;
        }
    }

}