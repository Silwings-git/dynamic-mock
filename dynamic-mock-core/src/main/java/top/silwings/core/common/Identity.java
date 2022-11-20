package top.silwings.core.common;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @ClassName Identity
 * @Description ID
 * @Author Silwings
 * @Date 2022/11/14 22:08
 * @Since
 **/
@JsonSerialize(using = Identity.IdentitySerializer.class)
@JsonDeserialize(using = Identity.IdentityDeserializer.class)
public class Identity {

    /**
     * 唯一标识符
     */
    private final long id;

    public Identity(final long id) {
        this.id = id;
    }

    public static Identity from(final Long id) {
        return new Identity(Objects.requireNonNull(id));
    }

    public static Identity from(final String id) {
        return Identity.from(Long.parseLong(id));
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Identity identity = (Identity) o;
        return Objects.equals(id, identity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return this.stringValue();
    }

    public long longValue() {
        return this.id;
    }

    public String stringValue() {
        return String.valueOf(this.longValue());
    }

    /**
     * @ClassName IdentitySerializer
     * @Description id序列化器
     * @Author Silwings
     * @Date 2022/11/20 18:35
     * @Since
     **/
    public static class IdentitySerializer extends StdSerializer<Identity> {

        public IdentitySerializer() {
            this(Identity.class);
        }

        public IdentitySerializer(final Class<Identity> t) {
            super(t);
        }

        @Override
        public void serialize(final Identity identity, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeNumber(identity.stringValue());
        }

    }

    /**
     * @ClassName IdentityDeserializer
     * @Description
     * @Author Silwings
     * @Date 2022/11/20 18:49
     * @Since
     **/
    public static class IdentityDeserializer extends JsonDeserializer<Identity> {

        @Override
        public Identity deserialize(final JsonParser jsonParser, final DeserializationContext context) throws IOException {
            return from(jsonParser.getValueAsString());
        }

    }

    public static class IdentityFastJsonObjectWriter implements ObjectWriter<Identity> {
        @Override
        public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
            if (null == object) {
                jsonWriter.writeNull();
            } else {
                if (object instanceof Identity) {
                    jsonWriter.writeString(((Identity) object).stringValue());
                }
            }
        }
    }

    public static class IdentityFastJsonObjectReader implements ObjectReader<Identity> {
        @Override
        public Identity readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
            final String id = jsonReader.readString();
            if (StringUtils.isBlank(id)) {
                return null;
            } else {
                return Identity.from(id);
            }
        }
    }

    public static class IdentityFastJsonObjectSerializer implements ObjectSerializer {
        @Override
        public void write(final JSONSerializer serializer, final Object object, final Object fieldName, final Type fieldType, final int features) {
            if (object instanceof Identity) {
                serializer.write(((Identity) object).stringValue());
            } else {
                serializer.write(object);
            }
        }
    }

    public static class IdentityFastJsonObjectDeserializer implements ObjectDeserializer {

        @Override
        public Identity deserialze(final DefaultJSONParser parser, final Type type, final Object fieldName) {

            final String id = parser.parseObject(String.class);

            if (StringUtils.isNotBlank(id)) {
                return Identity.from(id);
            }

            return null;
        }
    }

}