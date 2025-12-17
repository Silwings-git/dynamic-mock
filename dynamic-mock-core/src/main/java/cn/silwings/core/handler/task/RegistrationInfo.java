package cn.silwings.core.handler.task;

import lombok.Builder;
import lombok.Getter;

/**
 * @ClassName RegistrationInfo
 * @Description
 * @Author Silwings
 * @Date 2023/1/4 23:24
 * @Since
 **/
@Getter
@Builder
public class RegistrationInfo {

    private final String taskCode;

    private final long registrationTime;

}