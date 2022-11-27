package top.silwings.admin.auth;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import top.silwings.admin.common.Role;
import top.silwings.admin.config.DynamicMockAdminContext;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.admin.model.UserDto;
import top.silwings.core.common.Identity;
import top.silwings.core.utils.CheckUtils;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName UserAuthInfo
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 18:11
 * @Since
 **/
@Getter
@Builder
public class UserAuthInfo {

    /**
     * 用户id
     */
    private Identity userId;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户角色
     * {@link Role}
     */
    private int role;

    /**
     * 用户权限
     */
    private List<Identity> permissionList;

    /**
     * 用户拥有权限的handlerId集
     * 懒加载
     */
    private List<Identity> handlerIdList;

    public static UserAuthInfo from(final UserDto user) {
        return UserAuthInfo.builder()
                .userId(user.getUserId())
                .userAccount(user.getUserAccount())
                .role(user.getRole())
                .permissionList(queryPermission(user))
                .build();
    }

    private static List<Identity> queryPermission(final UserDto user) {
        if (Role.ADMIN_USER.equalsCode(user.getRole())) {
            return DynamicMockAdminContext.getInstance().getProjectService().queryAllProjectId();
        } else {
            return user.getPermissionList();
        }
    }

    /**
     * 判断登录用户是否是管理员
     *
     * @return true-是,false-否
     */
    public boolean isAdminUser() {
        return Role.ADMIN_USER.equalsCode(this.role);
    }

    /**
     * 校验项目权限
     *
     * @param projectId 项目id
     */
    public void validPermission(final Identity projectId) {
        if (!isAdminUser()) {
            CheckUtils.isIn(projectId, this.permissionList, DynamicMockAdminException.supplier(ErrorCode.AUTH_INSUFFICIENT_PERMISSIONS));
        }
    }

    /**
     * 校验项目权限
     *
     * @param projectId 项目id
     */
    public void validProjectId(final Identity projectId) {
        this.validPermission(projectId);
    }

    /**
     * 校验处理器权限
     *
     * @param handlerId 处理器id
     */
    public void validHandlerId(final Identity handlerId) {
        if (!isAdminUser()) {
            CheckUtils.isIn(handlerId, this.getHandlerIdList(), DynamicMockAdminException.supplier(ErrorCode.AUTH_INSUFFICIENT_PERMISSIONS));
        }
    }

    /**
     * 获取登录用户拥有权限的处理器id集
     */
    public List<Identity> getHandlerIdList() {
        if (CollectionUtils.isEmpty(this.permissionList)) {
            return Collections.emptyList();
        }
        if (null == this.handlerIdList) {
            this.handlerIdList = DynamicMockAdminContext.getInstance().getMockHandlerService().queryHandlerIds(this.permissionList);
        }
        return this.handlerIdList;
    }

    /**
     * 获取该用户拥有的项目列表
     */
    public List<Identity> getProjectIdList() {
        return this.permissionList;
    }

}