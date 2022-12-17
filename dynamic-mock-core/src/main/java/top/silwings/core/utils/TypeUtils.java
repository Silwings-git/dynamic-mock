package top.silwings.core.utils;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TypeUtils {

    private TypeUtils() {
        throw new AssertionError();
    }

    public static Integer toInteger(Object value) {
        if (value == null || value instanceof Integer) {
            return (Integer) value;
        }

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        if (value instanceof String) {
            String str = (String) value;
            if (str.isEmpty() || "null".equals(str)) {
                return null;
            }
            try {
                return Integer.parseInt(str.trim());
            } catch (Exception e) {
                throw TypeCastException.of("Can not cast to integer from" + value.getClass(), e);
            }
        }

        if (value instanceof Map && ((Map<?, ?>) value).isEmpty()) {
            return null;
        }

        if (value instanceof Boolean) {
            return (boolean) value ? 1 : 0;
        }

        throw TypeCastException.from("Can not cast to integer from" + value.getClass());
    }

    public static long toLong(final Object value) {

        if (value == null) {
            return 0L;
        }

        if (value instanceof Long) {
            return (Long) value;
        }

        if (value instanceof Number) {
            return ((Number) value).longValue();
        }

        if (value instanceof String) {
            String str = (String) value;
            if (str.isEmpty() || "null".equals(str)) {
                return 0;
            }
            try {
                return Long.parseLong(str.trim());
            } catch (Exception e) {
                throw TypeCastException.of("Can not cast to long from " + value.getClass(), e);
            }
        }

        throw TypeCastException.from("Can not cast to long from " + value.getClass());
    }


    public static double toDouble(final Object value) {

        if (value == null) {
            return 0D;
        }

        if (value instanceof Double) {
            return (Double) value;
        }

        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }

        if (value instanceof String) {
            String str = (String) value;
            if (str.isEmpty() || "null".equals(str)) {
                return 0D;
            }
            try {
                return Double.parseDouble(str.trim());
            } catch (Exception e) {
                throw TypeCastException.of("can not cast to decimal from" + value.getClass(), e);
            }
        }

        throw TypeCastException.from("can not cast to decimal from" + value.getClass());
    }

    public static BigDecimal toBigDecimal(final Object value) {
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
            try {
                return new BigDecimal(str.trim());
            } catch (Exception e) {
                throw TypeCastException.of("Can not cast to decimal from " + value.getClass(), e);
            }
        }

        throw TypeCastException.from("Can not cast to decimal from " + value.getClass());
    }

    public static boolean toBooleanValue(Object value) {
        if (value == null) {
            return false;
        }

        if (value instanceof Boolean) {
            return (Boolean) value;
        }

        if (value instanceof String) {
            String str = (String) value;
            if (str.isEmpty() || "null".equals(str)) {
                return false;
            }
            return Boolean.parseBoolean(str);
        }

        throw TypeCastException.from("Can not cast to boolean from " + value.getClass());
    }

}