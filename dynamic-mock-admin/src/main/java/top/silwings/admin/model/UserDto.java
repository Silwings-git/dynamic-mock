package top.silwings.admin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.silwings.admin.repository.po.UserPo;
import top.silwings.core.common.Identity;
import top.silwings.core.utils.ConvertUtils;
import top.silwings.core.utils.JsonUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName User
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 16:21
 * @Since
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private static final String DEFAULT_PASSWORD = "root";

    private Identity userId;

    private String username;

    private String userAccount;

    private String password;

    private int role;

    private List<Identity> permissionList;

    public static UserDto from(final UserPo userPo) {
        return UserDto.builder()
                .userId(Identity.from(userPo.getUserId()))
                .username(userPo.getUsername())
                .userAccount(userPo.getUserAccount())
                .password(userPo.getPassword())
                .role(userPo.getRole())
                .permissionList(ConvertUtils.getNoBlankOrDefault(userPo.getPermission(),
                        Collections.emptyList(),
                        permission -> Arrays.stream(permission.split(","))
                                .map(Identity::from)
                                .collect(Collectors.toList())))
                .build();
    }

    public static UserDto from(final String json) {
        return JsonUtils.toBean(json, UserDto.class);
    }

}