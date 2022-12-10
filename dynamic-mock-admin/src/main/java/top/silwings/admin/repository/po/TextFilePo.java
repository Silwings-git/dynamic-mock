package top.silwings.admin.repository.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName TextFilePo
 * @Description 文本文件内容
 * @Author Silwings
 * @Date 2022/12/10 12:56
 * @Since
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "dm_text_file")
public class TextFilePo {

    public static final String C_FILE_NAME = "fileName";

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "content")
    private String content;

    @Column(name = "create_time")
    private String createTime;

}