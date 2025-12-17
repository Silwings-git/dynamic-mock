package cn.silwings.core.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private final int id;

    public Identity(final int id) {
        this.id = id;
    }

    public static Identity from(final Integer id) {
        return new Identity(Objects.requireNonNull(id));
    }

    public static Identity from(final String id) {
        return Identity.from(Integer.parseInt(id));
    }

    public static List<Integer> toInt(final Collection<Identity> identities) {
        if (CollectionUtils.isEmpty(identities)) {
            return Collections.emptyList();
        }
        return identities.stream().map(Identity::intValue).collect(Collectors.toList());
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

    public int intValue() {
        return this.id;
    }

    public String stringValue() {
        return String.valueOf(this.intValue());
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
    @Slf4j
    public static class IdentityDeserializer extends JsonDeserializer<Identity> {

        @Override
        public Identity deserialize(final JsonParser jsonParser, final DeserializationContext context) throws IOException {
            final String valueAsString = jsonParser.getValueAsString();

            if (StringUtils.isBlank(valueAsString)) {
                return null;
            }

            try {
                return Identity.from(valueAsString);
            } catch (Exception e) {
                log.error("Identity 反序列化失败.", e);
                return null;
            }
        }

    }

}