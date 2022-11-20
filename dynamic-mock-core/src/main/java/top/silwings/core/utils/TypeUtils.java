package top.silwings.core.utils;

import top.silwings.core.exceptions.TypeCastException;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @ClassName TypeUtils
 * @Description 类型转换
 * @Author Silwings
 * @Date 2022/11/20 20:54
 * @Since
 **/
public class TypeUtils {
    public static Integer toInteger(Object value) {
        if (value == null || value instanceof Integer) {
            return (Integer) value;
        }

        if (value instanceof Number) {
            return Integer.valueOf(((Number) value).intValue());
        }

        if (value instanceof String) {
            String str = (String) value;
            if (str.isEmpty() || "null".equals(str)) {
                return null;
            }
            return Integer.parseInt(str);
        }

        if (value instanceof Map && ((Map<?, ?>) value).isEmpty()) {
            return null;
        }

        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue() ? 1 : 0;
        }

        throw new TypeCastException("can not cast to integer");
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

        throw new TypeCastException("can not cast to decimal from " + value.getClass());
    }

    public static boolean toBooleanValue(Object value) {
        if (value == null) {
            return false;
        }

        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }

        if (value instanceof String) {
            String str = (String) value;
            if (str.isEmpty() || "null".equals(str)) {
                return false;
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

        throw new TypeCastException("can not cast to boolean");
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

        throw new TypeCastException("can not cast to boolean");
    }

}