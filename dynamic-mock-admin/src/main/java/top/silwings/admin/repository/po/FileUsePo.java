package top.silwings.admin.repository.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @ClassName FileUsePo
 * @Description 文件使用表
 * @Author Silwings
 * @Date 2022/12/10 13:09
 * @Since
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "dm_file_use")
public class FileUsePo {

    @Column(name = "file_id")
    private Integer fileId;

    @Column(name = "handler_id")
    private Integer handlerId;

}