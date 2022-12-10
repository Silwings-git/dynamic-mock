package top.silwings.admin.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.IdGenerator;
import tk.mybatis.mapper.entity.Example;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.admin.model.TextFile;
import top.silwings.admin.repository.mapper.TextFileMapper;
import top.silwings.admin.repository.po.TextFilePo;
import top.silwings.admin.service.FileService;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.ConvertUtils;

import java.util.List;

/**
 * @ClassName FileServiceImpl
 * @Description 文件管理
 * @Author Silwings
 * @Date 2022/12/10 13:12
 * @Since
 **/
@Service
public class FileServiceImpl implements FileService {

    private final IdGenerator idGenerator;

    private final TextFileMapper textFileMapper;

    public FileServiceImpl(final IdGenerator idGenerator, final TextFileMapper textFileMapper) {
        this.idGenerator = idGenerator;
        this.textFileMapper = textFileMapper;
    }

    @Override
    public TextFile save(final String originalFilename, final String content) {

        final TextFilePo textFile = TextFilePo.builder()
                .fileName(this.idGenerator.generateId() + originalFilename.substring(originalFilename.lastIndexOf(".")))
                .originalFileName(originalFilename)
                .content(content)
                .build();

        this.textFileMapper.insertSelective(textFile);

        return TextFile.from(textFile);
    }

    @Override
    public TextFile find(final String fileName) {

        final Example example = new Example(TextFilePo.class);
        example.createCriteria()
                .andEqualTo(TextFilePo.C_FILE_NAME, ConvertUtils.getNoBlankOrDefault(fileName, ""));

        final List<TextFilePo> textFileList = this.textFileMapper.selectByConditionAndRowBounds(example, PageParam.oneRow());

        CheckUtils.isNotEmpty(textFileList, DynamicMockAdminException.supplier(ErrorCode.VALID_ERROR, fileName));

        return TextFile.from(textFileList.get(0));
    }

}