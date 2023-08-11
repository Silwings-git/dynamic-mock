package top.silwings.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import top.silwings.core.exceptions.DynamicMockException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @ClassName JsonUtils
 * @Description
 * @Author Silwings
 * @Date 2022/11/17 0:47
 * @Since
 **/
@Slf4j
public class JsonUtils {

    public static final String EMPTY_JSON = "{}";
    public static final String EMPTY_ARRAY = "[]";

    public static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Configuration DEFAULT_PATH_TO_NULL = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

    static {
        // json字符中存在实体中不包含的key时忽略
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JsonUtils() {
        throw new AssertionError();
    }

    public static String toJSONString(final Object body) {

        if (body == null) {
            return null;
        }
        if (body.getClass() == String.class) {
            return (String) body;
        }
        try {
            return MAPPER.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            log.error("Json serialization error: " + body, e);
            throw new DynamicMockException(e);
        }
    }

    public static String toJSONString(final Object body, final JsonInclude.Include include) {

        if (body == null) {
            return null;
        }
        if (body.getClass() == String.class) {
            return (String) body;
        }
        try {
            return new ObjectMapper().setSerializationInclusion(include).writeValueAsString(body);
        } catch (JsonProcessingException e) {
            log.error("Json serialization error: " + body, e);
            throw new DynamicMockException(e);
        }
    }

    public static <T> T toBean(final String jsonStr, final Class<T> tClass) {
        try {
            return MAPPER.readValue(jsonStr, tClass);
        } catch (IOException e) {
            log.error("Json parsing error: " + jsonStr, e);
            throw new DynamicMockException(e);
        }
    }

    public static <T> T tryToBean(final String jsonStr, final Class<T> tClass, final Supplier<T> defaultReturnValueSupplier) {
        try {
            return MAPPER.readValue(jsonStr, tClass);
        } catch (Exception e) {
            log.debug("tryToBean error: " + jsonStr, e);
            return defaultReturnValueSupplier.get();
        }
    }

    public static Object toBean(final String jsonStr) {
        try {
            return MAPPER.readValue(jsonStr, Object.class);
        } catch (IOException e) {
            log.error("Json parsing error: " + jsonStr, e);
            throw new DynamicMockException(e);
        }
    }

    public static Object tryToBean(final String jsonStr) {
        try {
            return MAPPER.readValue(jsonStr, Object.class);
        } catch (IOException e) {
            log.debug("tryToBean error: " + jsonStr, e);
            return jsonStr;
        }
    }

    public static boolean isValidJson(final String jsonStr) {

        if (StringUtils.isBlank(jsonStr)) {
            return false;
        }

        try {
            MAPPER.readTree(jsonStr);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    public static boolean isValidListJson(final String jsonStr) {
        return isValidJson(jsonStr) && jsonStr.startsWith("[");
    }


    public static <T, E> Map<T, E> toMap(final String jsonStr, final Class<T> keyClass, final Class<E> valueClass) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        try {
            return MAPPER.readValue(jsonStr, MAPPER.getTypeFactory().constructMapType(Map.class, keyClass, valueClass));
        } catch (IOException e) {
            log.error("Json parsing error: " + jsonStr, e);
            throw new DynamicMockException(e);
        }
    }

    public static <E> List<E> toList(final String jsonStr, final Class<E> eClass) {
        try {
            return MAPPER.readValue(jsonStr, MAPPER.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (IOException e) {
            log.error("Json parsing error: " + jsonStr, e);
            throw new DynamicMockException(e);
        }
    }

    public static <T> T nativeRead(final String jsonStr, final TypeReference<T> type) {
        try {
            return MAPPER.readValue(jsonStr, type);
        } catch (IOException e) {
            log.error("Json parsing error: " + jsonStr, e);
            throw new DynamicMockException(e);
        }
    }

    /**
     * 使用jsonPath从json对象读取数据
     *
     * @param obj      java bean
     * @param jsonPath json path
     * @return 如果找到返回路径元素, 找不到返回null
     */
    public static Object jsonPathRead(final Object obj, final String jsonPath) {

        final Object json = DEFAULT_PATH_TO_NULL.jsonProvider().parse(obj instanceof String ? (String) obj : toJSONString(obj));

        try {
            return JsonPath.read(json, jsonPath);
        } catch (Exception e) {
            // 匹配不到时返回 null
            return null;
        }
    }

}