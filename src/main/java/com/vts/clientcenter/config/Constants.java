package com.vts.clientcenter.config;

/**
 * Application constants.
 */
public final class Constants {
    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";
    public static final String PHONE_REGEX = "/^(1-?)?(\\([2-9]\\d{2}\\)|[2-9]\\d{2})-?[2-9]\\d{2}-?\\d{4}$/";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String JOB_PARAM_FILE_NAME = "employee-batch-loader.fileName";
    public static final String DATA_PROCESSING_GROUP_ID = "data-processing";
    public static final String MY_COUNTRY = "Viet Nam";
    public static final String NAME_EMPLOYER_EXISTED = "NAME_EMPLOYER_EXISTED";

    public static final String ID_NOT_NULL = "ID_NOT_NULL";
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String USER_ROLE_NOT_FOUND = "USER_ROLE_NOT_FOUND";
    public static final String USER_HAS_EXISTED = "USER_HAS_EXISTED";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String TOPIC_CREATE_USER_ACCOUNT_MAIL = "TOPIC_CREATE_USER_ACCOUNT_MAIL";
    public static final String ROLE_NOT_MATCH = "ROLE_NOT_MATCH";
    public static final String USER_CAN_NOT_CREATED = "USER_CAN_NOT_CREATED";
    public static final String USER_VALIDATOR_ERR = "USER_VALIDATOR_ERR";
    public static final String USER_UNAUTHORIZED = "USER_UNAUTHORIZED";
    public static final String PERMISSION_NOT_SUPPORTED = "PERMISSION_NOT_SUPPORTED";
    public static final String PERMISSION_IS_NULL = "PERMISSION_IS_NULL";
    public static final String ID_IS_NULL = "ID_IS_NULL";
    public static final String ROLE_NOT_DELETE = "ROLE_NOT_DELETE";
    public static final String ACCOUNT_STATUS_FIELD = "accountStatus";
    public static final String ACCOUNT_CREATION_MESSAGE = "You just create a new account.";
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    private Constants() {}
}
