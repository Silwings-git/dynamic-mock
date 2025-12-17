package cn.silwings.admin.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @ClassName CompressorUtils
 * @Description 压缩工具类
 * @Author Silwings
 * @Date 2023/8/9 15:40
 * @Since
 **/
public class CompressorUtils {

    private CompressorUtils() {
        throw new AssertionError();
    }

    public static byte[] gZipCompress(final String data) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
            gzipOutputStream.write(data.getBytes());
        }
        return outputStream.toByteArray();
    }

    public static String gZipDecompress(final byte[] compressedData) throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream)) {
            final byte[] buffer = new byte[1024];
            int bytesRead;
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = gzipInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, bytesRead);
            }
            return output.toString();
        }
    }
}