package top.silwings.admin.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.silwings.admin.common.DynamicMockBaseMapper;
import top.silwings.admin.repository.po.TextFilePo;

/**
 * @ClassName TextFileMapper
 * @Description 文本文件内容
 * @Author Silwings
 * @Date 2022/12/10 12:59
 * @Since
 **/
@Mapper
public interface TextFileMapper extends DynamicMockBaseMapper<TextFilePo> {
}