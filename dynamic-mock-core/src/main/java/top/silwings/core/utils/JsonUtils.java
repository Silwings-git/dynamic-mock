package top.silwings.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public static final ObjectMapper MAPPER = new ObjectMapper();

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

}