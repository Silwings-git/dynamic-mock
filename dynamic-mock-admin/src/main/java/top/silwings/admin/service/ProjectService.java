package top.silwings.admin.service;

import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.model.ProjectSummary;
import top.silwings.core.common.Identity;

/**
 * @ClassName ProjectService
 * @Description 项目管理
 * @Author Silwings
 * @Date 2022/11/20 13:54
 * @Since
 **/
public interface ProjectService {
    void save(Identity projectId, String projectName, String baseUri);

    void delete(Identity projectId);

    PageData<ProjectSummary> querySummary(String projectName, PageParam pageParam);

}