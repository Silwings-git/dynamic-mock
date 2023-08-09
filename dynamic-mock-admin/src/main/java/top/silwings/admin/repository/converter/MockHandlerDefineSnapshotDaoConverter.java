package top.silwings.admin.repository.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.admin.model.MockHandlerDefineSnapshotDto;
import top.silwings.admin.repository.po.MockHandlerDefineSnapshotPo;
import top.silwings.admin.utils.CompressorUtils;
import top.silwings.core.common.Identity;
import top.silwings.core.model.MockHandlerDto;
import top.silwings.core.utils.JsonUtils;

import java.io.IOException;

/**
 * @ClassName MockHandlerDefineSnapshotConverter
 * @Description
 * @Author Silwings
 * @Date 2023/8/9 15:57
 * @Since
 **/
@Slf4j
@Component
public class MockHandlerDefineSnapshotDaoConverter {

    public MockHandlerDefineSnapshotPo convert(final Identity handlerId, final String snapshotVersion, final MockHandlerDto mockHandlerDto, final Identity userId) {

        final byte[] snapshotData;
        try {
            snapshotData = CompressorUtils.gZipCompress(JsonUtils.toJSONString(mockHandlerDto));
        } catch (IOException e) {
            log.error("快照压缩失败.", e);
            throw DynamicMockAdminException.of(ErrorCode.UNKNOWN_ERROR, new String[0]);
        }

        final MockHandlerDefineSnapshotPo po = new MockHandlerDefineSnapshotPo();
        po.setSnapshotId(null);
        po.setHandlerId(handlerId.intValue());
        po.setSnapshotVersion(snapshotVersion);
        po.setSnapshotData(snapshotData);
        po.setCreateUser(userId.stringValue());

        return po;
    }

    public MockHandlerDefineSnapshotDto convert(final MockHandlerDefineSnapshotPo mockHandlerDefineSnapshotPo) {

        final byte[] snapshotData = mockHandlerDefineSnapshotPo.getSnapshotData();
        final String mockHandlerJson;
        try {
            mockHandlerJson = CompressorUtils.gZipDecompress(snapshotData);
        } catch (IOException e) {
            log.error("快照解压失败.", e);
            throw DynamicMockAdminException.of(ErrorCode.UNKNOWN_ERROR, new String[0]);
        }
        final MockHandlerDto mockHandlerDto = JsonUtils.nativeRead(mockHandlerJson, new TypeReference<MockHandlerDto>() {
        });

        final MockHandlerDefineSnapshotDto snapshotDto = new MockHandlerDefineSnapshotDto();
        snapshotDto.setSnapshotId(mockHandlerDefineSnapshotPo.getSnapshotId());
        snapshotDto.setHandlerId(mockHandlerDefineSnapshotPo.getHandlerId());
        snapshotDto.setSnapshotVersion(mockHandlerDefineSnapshotPo.getSnapshotVersion());
        snapshotDto.setMockHandlerDto(mockHandlerDto);
        snapshotDto.setCreateUser(mockHandlerDefineSnapshotPo.getCreateUser());
        snapshotDto.setCreateTime(mockHandlerDefineSnapshotPo.getCreateTime());

        return snapshotDto;
    }
}