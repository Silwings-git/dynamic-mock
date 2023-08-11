package top.silwings.core.utils;

import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Map;

/**
 * @ClassName HttpUtils
 * @Description
 * @Author Silwings
 * @Date 2023/8/11 16:30
 * @Since
 **/
public class HttpUtils {

    private HttpUtils() {
        throw new AssertionError();
    }

    public static HttpHeaders convertMapToHttpHeaders(final Map<String, List<String>> map) {
        final HttpHeaders httpHeaders = new HttpHeaders();

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            final String headerName = entry.getKey();
            final List<String> headerValues = entry.getValue();

            for (String value : headerValues) {
                httpHeaders.add(headerName, value);
            }
        }

        return httpHeaders;
    }

}