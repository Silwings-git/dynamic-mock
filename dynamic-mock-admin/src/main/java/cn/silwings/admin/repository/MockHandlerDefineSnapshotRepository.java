package cn.silwings.admin.repository;

import cn.silwings.admin.common.PageData;
import cn.silwings.admin.common.PageParam;
import cn.silwings.admin.model.MockHandlerDefineSnapshotDto;
import cn.silwings.core.common.Identity;
import cn.silwings.core.model.MockHandlerDto;

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