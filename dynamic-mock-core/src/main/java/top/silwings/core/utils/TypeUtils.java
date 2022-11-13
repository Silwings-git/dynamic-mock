package top.silwings.core.utils;

import com.alibaba.fastjson2.JSONException;

import java.math.BigDecimal;

/**
 * @ClassName TypeUtils
 * @Description
 * @Author Silwings
 * @Date 2022/11/6 14:07
 * @Since
 **/
public class TypeUtils {

    private TypeUtils() {
        throw new AssertionError();
    }

    public static BigDecimal toBigDecimal(Object value) {
        if (value == null || value instanceof BigDecimal) {
            return (BigDecimal) value;
        }

        if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long) {
            return BigDecimal.valueOf(((Number) value).longValue());
        }

        if (value instanceof String) {
            String str = (String) value;
            if (str.isEmpty() || "null".equals(str)) {
                return null;
            }
            return new BigDecimal(str);
        }

        throw new RuntimeException("can not cast to decimal from " + value.getClass());
    }

    public static Boolean toBoolean(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }

        if (value instanceof String) {
            String str = (String) value;
            if (str.isEmpty() || "null".equals(str)) {
                return null;
            }
            return Boolean.parseBoolean(str);
        }

        if (value instanceof Number) {
            int intValue = ((Number) value).intValue();
            if (intValue == 1) {
                return true;
            }
            if (intValue == 0) {
                return false;
            }
        }

        throw new JSONException("can not cast to boolean");
    }

}