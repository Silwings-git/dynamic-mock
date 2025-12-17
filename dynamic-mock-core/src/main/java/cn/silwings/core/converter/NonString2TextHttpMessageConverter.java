package cn.silwings.core.converter;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import cn.silwings.core.utils.JsonUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName NonString2TextHttpMessageConverter
 * @Description 非字符串响应数据转换为text的消息转换器, 不可读数据
 * @Author Silwings
 * @Date 2023/5/24 23:37
 * @Since
 **/
public class NonString2TextHttpMessageConverter implements HttpMessageConverter<Object> {

    private final List<MediaType> supportedMediaTypes;

    public NonString2TextHttpMessageConverter() {
        this.supportedMediaTypes = Stream.of(MediaType.TEXT_XML, MediaType.TEXT_PLAIN, MediaType.TEXT_HTML).collect(Collectors.toList());
    }

    @Override
    public boolean canRead(final Class<?> clazz, final MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(final Class<?> clazz, final MediaType mediaType) {
        // 检查是否可以写入指定的类到指定的媒体类型
        return mediaType != null
               && (mediaType.isCompatibleWith(MediaType.TEXT_XML) || mediaType.isCompatibleWith(MediaType.TEXT_HTML))
               && !String.class.isAssignableFrom(clazz);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        // 返回支持的媒体类型，只包括TEXT_XML
        return this.supportedMediaTypes;
    }

    @Override
    public Object read(final Class<?> clazz, final HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    public void write(final Object obj, final MediaType contentType, final HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        // 将对象转换为字符串，并写入响应体
        final String resString = JsonUtils.toJSONString(obj);
        final byte[] bytes = resString.getBytes(StandardCharsets.UTF_8);
        outputMessage.getBody().write(bytes);
    }
}
