package top.silwings.admin.exceptions;

import lombok.Getter;

/**
 * @ClassName ErrorCodes
 * @Description
 * @Author Silwings
 * @Date 2022/11/22 22:33
 * @Since
 **/
@Getter
public enum ErrorCode {

    // Common
    UNKNOWN_ERROR("unknown_error"),
    VALID_EMPTY("valid_empty"),
    VALID_ERROR("valid_error"),
    VALID_TOO_LONG("valid_too_long"),
    EXPRESSION_INCORRECT("expression_incorrect"),

    // Login
    LOGIN_ACCOUNT_PASSWORD_INCORRECT("login_account_password_incorrect"),

    // Mock Handler
    MOCK_HANDLER_DUPLICATE_REQUEST_PATH("mock_handler_duplicate_request_path"),
    MOCK_HANDLER_NOT_EXIST("mock_handler_not_exist"),
    MOCK_HANDLER_RESPONSE_NOT_EXIST("mock_handler_response_not_exist"),
    MOCK_HANDLER_CONCURRENT_ERROR("mock_handler_concurrent_error"),

    MOCK_HANDLER_PROJECT_MISMATCH("mock_handler_project_mismatch"),

    MOCK_TASK_LOG_NOT_EXIST("mock_task_log_not_exist"),

    // Project
    PROJECT_PROHIBIT_DELETION("project_prohibit_deletion"),
    PROJECT_NOT_EXIST("project_not_exist"),
    PROJECT_DUPLICATE_BASE_URI("project_duplicate_base_uri"),

    // User
    USER_DUPLICATE_ACCOUNT("user_duplicate_account"),
    USER_OLD_PASSWORD_ERROR("user_old_password_error"),
    USER_NOT_EXIST("user_not_exist"),
    USER_UPDATE_LOGIN_USER_LIMIT("user_update_login_user_limit"),

    // Auth
    AUTH_INSUFFICIENT_PERMISSIONS("auth_insufficient_permissions"),
    AUTH_NOT_LOGGED_ON("auth_not_logged_on"),

    // File
    UNSUPPORTED_FILE_TYPE("unsupported_file_type"),
    CONTENT_FORMAT_ERROR("content_format_error"),
    FILE_READ_ERROR("file_read_error"),
    FILE_NOT_FOUND("file_not_found"),

    // snapshot
    MOCK_HANDLER_DEFINE_SNAPSHOT_NOT_EXIST("mock_handler_define_snapshot_not_exist");

    private final String code;

    ErrorCode(final String code) {
        this.code = code;
    }

}
