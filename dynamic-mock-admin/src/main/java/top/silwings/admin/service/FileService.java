package top.silwings.admin.service;

import top.silwings.admin.model.TextFile;

/**
 * @ClassName FileService
 * @Description 文件管理
 * @Author Silwings
 * @Date 2022/12/10 13:11
 * @Since
 **/
public interface FileService {

    TextFile save(String originalFilename, String content);

    TextFile find(String fileName);
}