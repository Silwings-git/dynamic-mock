package top.silwings.core.interpreter.dynamic_expression.function.function_factory;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.context.MockHandlerContext;
import top.silwings.core.interpreter.ExpressionTreeNode;
import top.silwings.core.interpreter.dynamic_expression.function.AbstractFunctionExpression;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionInfo;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionReturnType;
import top.silwings.core.utils.CheckUtils;

import java.time.Duration;
import java.time.Instant;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @ClassName TimeShiftFunctionFactory
 * @Description 时移函数
 * @Author Silwings
 * @Date 2023/6/16 22:08
 * @Since
 **/
@Component
public class TimeShiftFunctionFactory implements FunctionFactory {

    private static final FunctionInfo TIME_SHIFT_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("TimeShift")
            .minArgsNumber(4)
            .maxArgsNumber(4)
            .functionReturnType(FunctionReturnType.STRING)
            .build();

    private static final String SYMBOL = "timeShift(,,,)";

    private static final TimeShiftFunctionCache TIME_SHIFT_FUNCTION_CACHE = new TimeShiftFunctionCache();

    @Override
    public boolean support(final String methodName) {
        return "timeShift".equalsIgnoreCase(methodName);
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return TIME_SHIFT_FUNCTION_INFO;
    }

    @Override
    public TimeShiftFunction buildFunction(final List<ExpressionTreeNode> expressionList) {
        return TimeShiftFunction.build(expressionList);
    }

    /**
     * 时移函数
     * #timeshift(Format,DateToShift,ValueToShift,LocaleToUseForFormat)
     * Format: 日期格式,不指定会返回毫秒值
     * DateToShift: 要转换的日期,不指定默认为当前时间
     * ValueToShift: 要移动的时间数.格式参考: https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html#parse-java.lang.CharSequence-
     * LocaleToUseForFormat: 语言环境的字符串格式，如：zh_CN,en_US
     * 不传值,使用默认值的,也需要标记参数分隔符",",如: #timeshift('yyyy-MM-dd HH:mm:ss',,'P10DT-1H-5M5S',)
     */
    @Slf4j
    public static class TimeShiftFunction extends AbstractFunctionExpression {

        protected TimeShiftFunction(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        public static TimeShiftFunction build(final List<ExpressionTreeNode> expressionList) {
            CheckUtils.sizeBetween(expressionList, TIME_SHIFT_FUNCTION_INFO.getMinArgsNumber(), TIME_SHIFT_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of TimeShift function."));
            return new TimeShiftFunction(expressionList);
        }

        @Override
        protected String doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            final String format = String.valueOf(childNodeValueList.get(0));
            final String dateToShift = String.valueOf(childNodeValueList.get(1));
            final String amountToShift = String.valueOf(childNodeValueList.get(2));
            final String localeAsString = String.valueOf(childNodeValueList.get(3));

            final Locale locale = TIME_SHIFT_FUNCTION_CACHE.getLocale(localeAsString, Locale::getDefault, str -> new Locale(str));

            final DateTimeFormatter formatter;
            if (StringUtils.isNotBlank(format)) {
                try {
                    formatter = TIME_SHIFT_FUNCTION_CACHE.getDateTimeFormatter(new LocaleFormatObject(format, locale), TimeShiftFunction::createFormatter);
                } catch (Exception ex) {
                    log.error("Format date pattern '{}' is invalid "
                              + "(see https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html)",
                            format, ex);
                    throw new DynamicMockException("Format date pattern " + format + " is invalid");
                }
            } else {
                formatter = null;
            }

            ZonedDateTime zonedDateTimeToShift = ZonedDateTime.now();

            if (StringUtils.isNotBlank(dateToShift)) {
                try {
                    if (formatter != null) {
                        zonedDateTimeToShift = ZonedDateTime.parse(dateToShift, formatter);
                    } else {
                        zonedDateTimeToShift = ZonedDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(dateToShift)), ZoneId.systemDefault());
                    }
                } catch (Exception ex) {
                    log.error("Failed to parse the date '{}' to shift with formatter '{}'", dateToShift, formatter, ex);
                    throw new DynamicMockException("Failed to parse the date " + dateToShift + " to shift with formatter " + formatter);
                }
            }

            if (StringUtils.isNotBlank(amountToShift)) {
                try {
                    final Duration duration = Duration.parse(amountToShift);
                    zonedDateTimeToShift = zonedDateTimeToShift.plus(duration);
                } catch (Exception ex) {
                    log.error(
                            "Failed to parse the amount duration '{}' to shift "
                            + "(see https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html#parse-java.lang.CharSequence-) ",
                            amountToShift, ex);
                    throw new DynamicMockException("Failed to parse the amount duration " + amountToShift + " to shift ");
                }
            }

            String dateString;
            if (formatter != null) {
                dateString = zonedDateTimeToShift.format(formatter);
            } else {
                dateString = String.valueOf(zonedDateTimeToShift.toInstant().toEpochMilli());
            }

            return dateString;
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }

        /**
         * 创建时间格式化类.
         * 该方法实现和jmeter保持一致
         *
         * @param format 格式
         * @param locale 地区信息
         * @return DateTimeFormatter
         */
        @SuppressWarnings("JavaTimeDefaultTimeZone")
        private static DateTimeFormatter createFormatter(final LocaleFormatObject localeFormatObject) {
            return new DateTimeFormatterBuilder()
                    .appendPattern(localeFormatObject.getFormat())
                    .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
                    .parseDefaulting(ChronoField.MILLI_OF_SECOND, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                    .parseDefaulting(ChronoField.YEAR_OF_ERA, Year.now().getValue())
                    .parseDefaulting(ChronoField.OFFSET_SECONDS, ZonedDateTime.now().getOffset().getTotalSeconds())
                    .toFormatter(localeFormatObject.getLocale());
        }
    }

    private static class TimeShiftFunctionCache {

        // locale缓存,key为地区标识符
        private final Map<String, Locale> localeMap = new ConcurrentHashMap<>();
        // 格式化类缓存.
        private final Map<LocaleFormatObject, DateTimeFormatter> localeFormatterMap = new ConcurrentHashMap<>();

        public Locale getLocale(final String localeAsString, final Supplier<Locale> defaultSupplier, final Function<String, Locale> localeFunction) {

            if (StringUtils.isBlank(localeAsString)) {
                return defaultSupplier.get();
            }

            final Locale locale = this.localeMap.get(localeAsString);
            if (null != locale) {
                return locale;
            }

            final Locale applyLocale = localeFunction.apply(localeAsString);
            this.localeMap.put(localeAsString, applyLocale);

            return applyLocale;
        }

        public DateTimeFormatter getDateTimeFormatter(final LocaleFormatObject localeFormatObject, final Function<LocaleFormatObject, DateTimeFormatter> createFormatter) {
            final DateTimeFormatter dateTimeFormatter = this.localeFormatterMap.get(localeFormatObject);
            if (null != dateTimeFormatter) {
                return dateTimeFormatter;
            }

            final DateTimeFormatter apply = createFormatter.apply(localeFormatObject);
            this.localeFormatterMap.put(localeFormatObject, apply);

            return apply;
        }
    }

    @Getter
    private static final class LocaleFormatObject {

        /**
         * 时间格式
         */
        private final String format;

        /**
         * 地区信息
         */
        private final Locale locale;

        public LocaleFormatObject(final String format, final Locale locale) {
            this.format = format;
            this.locale = locale;
        }

        @Override
        public int hashCode() {
            return this.format.hashCode() + this.locale.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            if (!(other instanceof LocaleFormatObject)) {
                return false;
            }

            final LocaleFormatObject otherError = (LocaleFormatObject) other;
            return this.format.equals(otherError.getFormat())
                   && this.locale.getDisplayName().equals(otherError.getLocale().getDisplayName());
        }

        @Override
        public String toString() {
            return "LocaleFormatObject [format=" + this.format + ", locale=" + this.locale + "]";
        }
    }

}