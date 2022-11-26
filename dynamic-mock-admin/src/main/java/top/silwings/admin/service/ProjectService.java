package top.silwings.admin.service;

import top.silwings.admin.model.ProjectDto;
import top.silwings.core.common.Identity;

import java.util.List;

/**
 * @ClassName ProjectService
 * @Description 项目管理
 * @Author Silwings
 * @Date 2022/11/20 13:54
 * @Since
 **/
public interface ProjectService {
    Identity create(String projectName, String baseUri);

    void delete(Identity projectId);

    List<ProjectDto> query(List<Identity> projectIdList, String projectName);

    Identity updateById(Identity projectId, String projectName, String baseUri);

    ProjectDto find(Identity projectId);

}