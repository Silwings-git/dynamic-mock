package top.silwings.admin.scheduled;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import top.silwings.admin.repository.mapper.MockHandlerMapper;
import top.silwings.admin.repository.mapper.TextFileMapper;
import top.silwings.admin.repository.po.MockHandlerPo;
import top.silwings.admin.repository.po.TextFilePo;
import top.silwings.admin.service.MockHandlerRegisterService;
import top.silwings.core.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName FileCleanUpScheduled
 * @Description 文件清理
 * @Author Silwings
 * @Date 2022/12/10 15:01
 * @Since
 **/
@Component
public class FileCleanUpScheduled {

    private final MockHandlerMapper mockHandlerMapper;

    private final TextFileMapper textFileMapper;

    public FileCleanUpScheduled(final MockHandlerMapper mockHandlerMapper, final TextFileMapper textFileMapper) {
        this.mockHandlerMapper = mockHandlerMapper;
        this.textFileMapper = textFileMapper;
    }

    @Scheduled(cron = "0 0/2 * * * ?")
    public void fileCleanUp() {

        final List<String> fileNameList = this.queryHandlerUsedFileNames();

        this.removeFile(fileNameList);
    }

    private void removeFile(final List<String> useFileNameList) {

        final Example example = new Example(TextFilePo.class);

        if (CollectionUtils.isNotEmpty(useFileNameList)) {
            example.createCriteria()
                    .andNotIn(TextFilePo.C_FILE_NAME, useFileNameList);
        }

        this.textFileMapper.deleteByCondition(example);
    }

    private List<String> queryHandlerUsedFileNames() {

        final Example example = new Example(MockHandlerPo.class);
        example.createCriteria()
                .andLike(MockHandlerPo.C_CUSTOMIZE_SPACE, "%" + MockHandlerRegisterService.FILE_FLAG + "%");

        example.selectProperties(MockHandlerPo.C_CUSTOMIZE_SPACE);

        return this.mockHandlerMapper.selectByCondition(example)
                .stream()
                .filter(e -> StringUtils.isNotBlank(e.getCustomizeSpace()))
                .map(e -> JsonUtils.toMap(e.getCustomizeSpace(), String.class, Object.class))
                .filter(Objects::nonNull)
                .map(Map::values)
                .reduce(new ArrayList<>(), (list, values) -> {
                    list.addAll(values);
                    return list;
                })
                .stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .filter(e -> e.contains(MockHandlerRegisterService.FILE_FLAG))
                .map(e -> e.replace(MockHandlerRegisterService.FILE_FLAG, "").trim())
                .distinct()
                .collect(Collectors.toList());
    }

}