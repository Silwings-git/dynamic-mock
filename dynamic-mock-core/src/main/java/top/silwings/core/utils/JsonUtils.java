package top.silwings.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.extern.slf4j.Slf4j;
import top.silwings.core.exceptions.DynamicMockException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName JsonUtils
 * @Description
 * @Author Silwings
 * @Date 2022/11/17 0:47
 * @Since
 **/
@Slf4j
public class JsonUtils {

    private static final Configuration DEFAULT_PATH_TO_NULL = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);


    public static final ObjectMapper MAPPER = new ObjectMapper();

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

    public static <T> T toBean(final String jsonStr, final Class<T> tClass) {
        try {
            return MAPPER.readValue(jsonStr, tClass);
        } catch (IOException e) {
            log.error("Json parsing error: " + jsonStr, e);
            throw new DynamicMockException(e);
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

    public static boolean isValidJson(final String jsonStr) {
        try {
            MAPPER.readTree(jsonStr);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }


    public static <T, E> Map<T, E> toMap(final String jsonStr, final Class<T> keyClass, final Class<E> valueClass) {
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

        final Object json = DEFAULT_PATH_TO_NULL.jsonProvider().parse(toJSONString(obj));

        return JsonPath.read(json, jsonPath);
    }

}