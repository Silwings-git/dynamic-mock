package top.silwings.admin.repository;

import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.model.MockHandlerDefineSnapshotDto;
import top.silwings.core.common.Identity;
import top.silwings.core.model.MockHandlerDto;

import java.util.Collection;

/**
 * @ClassName MockHandlerDefineSnapshotService
 * @Description
 * @Author Silwings
 * @Date 2023/8/9 15:25
 * @Since
 **/
public interface MockHandlerDefineSnapshotRepository {
    void snapshot(Identity handlerId, MockHandlerDto mockHandlerDto);

    MockHandlerDefineSnapshotDto findLast(String snapshotVersion);

    PageData<MockHandlerDefineSnapshotDto> query(Collection<Identity> handlerIds, PageParam pageParam);
}