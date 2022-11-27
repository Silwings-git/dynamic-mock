package top.silwings.admin.model;

import lombok.Builder;
import lombok.Getter;
import top.silwings.admin.repository.po.MockHandlerPo;
import top.silwings.core.common.Identity;

/**
 * @ClassName HandlerInfo
 * @Description
 * @Author Silwings
 * @Date 2022/11/27 12:11
 * @Since
 **/
@Getter
@Builder
public class HandlerInfoDto {

    /**
     * 项目id
     */
    private Identity projectId;

    /**
     * 处理器id
     */
    private Identity handlerId;

    /**
     * 处理器名称
     */
    private String name;

    public static HandlerInfoDto from(final MockHandlerPo po) {
        return HandlerInfoDto.builder()
                .projectId(Identity.from(po.getProjectId()))
                .handlerId(Identity.from(po.getHandlerId()))
                .name(po.getName())
                .build();
    }
}