package com.vts.clientcenter.config;

/**
 * Application constants.
 */
public final class Constants {
    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

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
    private Constants() {}
}
