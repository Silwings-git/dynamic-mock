package top.silwings.admin.utils;

import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import org.apache.commons.lang3.StringUtils;
import top.silwings.admin.common.Payload;
import top.silwings.core.utils.JsonUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName JwtUtils
 * @Description Jwt工具类
 * @Author Silwings
 * @Date 2022/11/19 12:52
 * @Since
 **/
public class JwtUtils {

    private static final Decoder<String, byte[]> STRING_DECODER = Decoders.BASE64URL;

    /**
     * 获取token中的载荷信息
     *
     * @param token 用户请求中的令牌
     * @return 用户信息
     */
    @SuppressWarnings("unchecked")
    public static <T> Payload<T> getInfoFromToken(final String token, final Class<T> userType) {

        final String payloadStr = StringUtils.substringBetween(token, ".");

        final byte[] bytes = STRING_DECODER.decode(payloadStr);

        final String json = new String(bytes, StandardCharsets.UTF_8);

        final Map<String, String> map = JsonUtils.toMap(json, String.class, String.class);

        final Payload<T> claims = new Payload<>();

        claims.setId(map.get("jti"));
        claims.setExpiration(new Date(Long.parseLong(map.getOrDefault("exp", "0"))));

        if (userType == String.class) {
            claims.setUserInfo((T) map.get("user"));
        } else {
            claims.setUserInfo(JsonUtils.toBean(map.get("user"), userType));
        }

        return claims;
    }
}