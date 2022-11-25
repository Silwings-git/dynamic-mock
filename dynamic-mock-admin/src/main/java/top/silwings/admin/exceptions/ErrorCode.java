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
    EXPRESSION_INCORRECT("expression_incorrect"),

    // Login
    LOGIN_ACCOUNT_PASSWORD_INCORRECT("login_account_password_incorrect"),

    // Mock Handler
    MOCK_HANDLER_DUPLICATE_REQUEST_PATH("mock_handler_duplicate_request_path"),
    MOCK_HANDLER_NOT_EXIST("mock_handler_not_exist"),

    MOCK_HANDLER_PROJECT_MISMATCH("mock_handler_project_mismatch"),

    // Project
    PROJECT_PROHIBIT_DELETION("project_prohibit_deletion"),
    PROJECT_NOT_EXIST("project_not_exist"),

    // User
    USER_DUPLICATE_ACCOUNT("user_duplicate_account"),
    USER_OLD_PASSWORD_ERROR("user_old_password_error"),
    USER_NOT_EXIST("user_not_exist"),
    USER_UPDATE_LOGIN_USER_LIMIT("user_update_login_user_limit"),

    // Auth
    AUTH_INSUFFICIENT_PERMISSIONS("auth_insufficient_permissions"),
    AUTH_NOT_LOGGED_ON("auth_not_logged_on"),


    ;

    private final String code;

    ErrorCode(final String code) {
        this.code = code;
    }

}
