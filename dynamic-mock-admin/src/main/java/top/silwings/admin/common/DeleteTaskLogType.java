package top.silwings.admin.common;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.function.Supplier;

/**
 * @ClassName DeleteTaskLogType
 * @Description 清理任务日志方式
 * @Author Silwings
 * @Date 2022/11/28 1:24
 * @Since
 **/
public enum DeleteTaskLogType {

    // 清理一个月之前的数据
    MONTH_1("1", () -> DateUtils.addMonths(new Date(), -1), () -> 0),

    // 清理三个月之前的数据
    MONTH_3("2", () -> DateUtils.addMonths(new Date(), -3), () -> 0),

    // 清理六个月之前的数据
    MONTH_6("3", () -> DateUtils.addMonths(new Date(), -6), () -> 0),

    // 清理一年之前的数据
    MONTH_12("4", () -> DateUtils.addYears(new Date(), -1), () -> 0),

    // 清理一千条之前的数据
    BEFORE_1_000("5", Date::new, () -> 1_000),

    // 清理一万条之前的数据
    BEFORE_10_000("6", Date::new, () -> 10_000),

    // 清理三万条之前的数据
    BEFORE_30_000("7", Date::new, () -> 30_000),

    // 清理十万条之前的数据
    BEFORE_100_000("8", Date::new, () -> 100_000),

    // 清理所有日志
    ALL("100", Date::new, () -> 0),
    ;

    private final String code;
    private final Supplier<Date> beforeTimeFun;
    private final Supplier<Integer> beforeNumFun;

    DeleteTaskLogType(final String code, final Supplier<Date> beforeTimeFun, final Supplier<Integer> beforeNumFun) {
        this.code = code;
        this.beforeTimeFun = beforeTimeFun;
        this.beforeNumFun = beforeNumFun;
    }

    public static DeleteTaskLogType valueOfCode(final String deleteType) {
        for (final DeleteTaskLogType value : values()) {
            if (value.code.equals(deleteType)) {
                return value;
            }
        }
        return null;
    }

    public TypeCondition typeCondition() {

        return TypeCondition.builder()
                .beforeTime(this.beforeTimeFun.get())
                .beforeNum(this.beforeNumFun.get())
                .build();
    }

    public String code() {
        return this.code;
    }

    @Getter
    @Builder
    public static class TypeCondition {

        /**
         * 指定日期之前
         */
        private final Date beforeTime;

        /**
         * 指定数量之前
         */
        private final int beforeNum;

    }

}
