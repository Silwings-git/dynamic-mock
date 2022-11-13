package top.silwings.core.converter;

import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Map;

/**
 * @ClassName HttpHeaderConverter
 * @Description
 * @Author Silwings
 * @Date 2022/11/13 9:34
 * @Since
 **/
public class HttpHeaderConverter {

    public static HttpHeaders from(final Object obj) {

        final HttpHeaders httpHeaders = new HttpHeaders();

        if (obj instanceof Map) {

            for (final Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {

                final String headerName = String.valueOf(entry.getKey());
                final Object entryValue = entry.getValue();

                if (entryValue instanceof List) {

                    ((List<?>) entryValue).forEach(headerValue -> httpHeaders.add(headerName, String.valueOf(headerValue)));
                } else {

                    httpHeaders.add(headerName, String.valueOf(entryValue));
                }
            }
        }

        return httpHeaders;
    }

}