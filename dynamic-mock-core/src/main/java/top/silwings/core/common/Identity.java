package top.silwings.core.common;

import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * @ClassName Identity
 * @Description ID
 * @Author Silwings
 * @Date 2022/11/14 22:08
 * @Since
 **/
public class Identity {

    /**
     * 唯一标识符
     */
    @ApiModelProperty(value = "唯一标识符", example = "Code001")
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

}