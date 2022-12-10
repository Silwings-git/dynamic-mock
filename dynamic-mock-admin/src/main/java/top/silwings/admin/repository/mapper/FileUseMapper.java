package top.silwings.admin.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.silwings.admin.common.DynamicMockBaseMapper;
import top.silwings.admin.repository.po.FileUsePo;

/**
 * @ClassName FileUseMapper
 * @Description 文件使用
 * @Author Silwings
 * @Date 2022/12/10 13:11
 * @Since
 **/
@Mapper
public interface FileUseMapper extends DynamicMockBaseMapper<FileUsePo> {
}