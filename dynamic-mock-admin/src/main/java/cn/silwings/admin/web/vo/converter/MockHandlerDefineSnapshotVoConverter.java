package cn.silwings.admin.web.vo.converter;

import org.springframework.stereotype.Component;
import cn.silwings.admin.model.MockHandlerDefineSnapshotDto;
import cn.silwings.admin.web.vo.result.MockHandlerDefineSnapshotResult;

/**
 * @ClassName MockHandlerDefineSnapshotVoConverter
 * @Description
 * @Author Silwings
 * @Date 2023/8/9 17:02
 * @Since
 **/
@Component
public class MockHandlerDefineSnapshotVoConverter {

    private final MockHandlerVoConverter mockHandlerVoConverter;

    public MockHandlerDefineSnapshotVoConverter(final MockHandlerVoConverter mockHandlerVoConverter) {
        this.mockHandlerVoConverter = mockHandlerVoConverter;
    }

    public MockHandlerDefineSnapshotResult convert(final MockHandlerDefineSnapshotDto snapshotDto) {

        if (null == snapshotDto) {
            return null;
        }

        final MockHandlerDefineSnapshotResult result = new MockHandlerDefineSnapshotResult();
        result.setSnapshotId(snapshotDto.getSnapshotId());
        result.setHandlerId(snapshotDto.getHandlerId());
        result.setSnapshotVersion(snapshotDto.getSnapshotVersion());
        result.setMockHandlerInfo(this.mockHandlerVoConverter.convert(snapshotDto.getMockHandlerDto(), null));
        result.setCreateUser(snapshotDto.getCreateUser());
        result.setCreateTime(snapshotDto.getCreateTime());

        return result;
    }
}