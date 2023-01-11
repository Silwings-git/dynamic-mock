package top.silwings.core.interpreter.dynamic_expression.parser;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.interpreter.Parser;

/**
 * @ClassName AutoTypeParser
 * @Description 自动类型解析
 * @Author Silwings
 * @Date 2023/1/4 11:06
 * @Since
 **/
@Component
public class AutoTypeParser implements Parser<Object, Object> {

    private final DynamicExpressionStringParser dynamicExpressionStringParser;

    public AutoTypeParser(final DynamicExpressionStringParser dynamicExpressionStringParser) {
        this.dynamicExpressionStringParser = dynamicExpressionStringParser;
    }

    @Override
    public boolean support(final Object o) {
        return true;
    }

    @Override
    public Object parse(final Object obj) {

        if (obj instanceof String) {

            return this.parseString((String) obj);

        }

        return obj;
    }

    private Object parseString(final String text) {

        if (StringUtils.isBlank(text)) {
            return text;
        }

        if (this.dynamicExpressionStringParser.support(text)) {
            return this.dynamicExpressionStringParser.parse(text);
        }

        // 不是单引号标识的字符串时尝试转换为布尔或数值
        if (Boolean.TRUE.toString().equals(text)) {
            return true;
        } else if (Boolean.FALSE.toString().equals(text)) {
            return false;
        } else {

            // 数值类型默认使用int,长度不满足使用long,浮点类型使用double
            if (text.indexOf(".") > 0) {
                return Double.parseDouble(text);
            } else {
                try {
                    return Integer.parseInt(text);
                } catch (NumberFormatException ex) {
                    try {
                        return Long.parseLong(text);
                    } catch (NumberFormatException e) {
                        throw DynamicValueCompileException.from("Illegal character : " + text);
                    }
                }
            }
        }
    }
}