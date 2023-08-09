package top.silwings.admin.utils;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName EncryptUtils
 * @Description 加密工具
 * @Author Silwings
 * @Date 2022/11/20 10:08
 * @Since
 **/
public class EncryptUtils {

    private EncryptUtils() {
        throw new AssertionError();
    }

    public static String encryptPassword(final String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
    }

}