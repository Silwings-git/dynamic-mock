package cn.silwings.core.converter;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName UriVariableConvertor
 * @Description
 * @Author Silwings
 * @Date 2022/11/13 9:52
 * @Since
 **/
public class UriVariableConvertor {

    private UriVariableConvertor() {
        throw new AssertionError();
    }

    public static Map<String, ?> from(final Object obj) {

        final MultiValueMap<String, String> uriVariables = new LinkedMultiValueMap<>();

        if (obj instanceof Map) {

            for (final Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {

                final String paramName = String.valueOf(entry.getKey());
                final Object paramValue = entry.getValue();

                if (paramValue instanceof List) {
                    final List<String> paramValueList = ((List<?>) paramValue).stream().map(String::valueOf).collect(Collectors.toList());
                    uriVariables.put(paramName, paramValueList);
                } else {
                    uriVariables.add(paramName, String.valueOf(paramValue));
                }
            }
        }

        return uriVariables;
    }

}