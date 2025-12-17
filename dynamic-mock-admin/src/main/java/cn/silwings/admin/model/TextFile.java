package cn.silwings.admin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import cn.silwings.admin.repository.po.TextFilePo;

/**
 * @ClassName TextFile
 * @Description 文本文件
 * @Author Silwings
 * @Date 2022/12/10 14:08
 * @Since
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TextFile {

    private String fileName;

    private String originalFileName;

    private String content;

    public static TextFile from(final TextFilePo po) {
        return TextFile.builder()
                .fileName(po.getFileName())
                .originalFileName(po.getOriginalFileName())
                .content(po.getContent())
                .build();
    }

    public String getSuffixName() {
        return this.fileName.substring(this.fileName.lastIndexOf("."));
    }
}