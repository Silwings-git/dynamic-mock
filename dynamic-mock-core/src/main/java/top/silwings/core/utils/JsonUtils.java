package top.silwings.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @ClassName JsonUtils
 * @Description
 * @Author Silwings
 * @Date 2022/11/17 0:47
 * @Since
 **/
public class JsonUtils {

    private JsonUtils() {
        throw new AssertionError();
    }

    public static String toJSONString(final Object body) {
        return JSON.toJSONString(body, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static String toJSONString(final Object body, final SerializerFeature feature) {
        return JSON.toJSONString(body, feature);
    }
}