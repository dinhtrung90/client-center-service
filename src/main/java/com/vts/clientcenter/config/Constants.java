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
    public static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";
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
    public static final String ACCOUNT_GENDER_FIELD = "gender";
    public static final String ACCOUNT_PHONE_FIELD = "phone";
    public static final String ACCOUNT_UPDATED_AT_FLAG_FIELD = "updated_at_flag";
    public static final String ACCOUNT_APPROVAL_FIELD = "is_approved";
    public static final String ACCOUNT_CREATION_MESSAGE = "You just create a new account.";
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_STANDARD_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String USER_STATUS_NOT_NULL = "USER_STATUS_NOT_NULL";
    public static final String USER_ADDRESS_NOT_FOUND = "USER_ADDRESS_NOT_FOUND";
    public static final String ACCOUNT_HOME_PHONE_FIELD = "home_phone";
    public static final String ACCOUNT_LANG_KEY_FIELD = "lang_key";
    public static final String ACCOUNT_IS_TERMINATED_FIELD = "is_terminated";
    public static final String ROLE_NAME_NOT_NULL = "ROLE_NAME_NOT_NULL";
    public static final String ORGANIZATION_NAME_EXISTED = "ORGANIZATION_NAME_EXISTED";
    public static final String ORGANIZATION_NOT_FOUND = "ORGANIZATION_NOT_FOUND";
    public static final String PRIMARY_COLOR = "#f23f23";
    public static final String BACKGROUND_COLOR = "#1d1d1d";
    public static final String OPENID_CONNECT = "openid-connect";
    public static final String SERVER_DOMAIN = "fingmeup.com";
    public static final String SERVER_PROTOCOL = "https://";
    public static final String PREFIX_ROLE_ACCESS = "ROLE_ACCESS_";
    public static final String EMAIL_HAS_EXISTED = "EMAIL_HAS_EXISTED";
    public static final String PHONE_HAS_EXISTED = "PHONE_HAS_EXISTED";
    public static final Integer EXPIRED_DAY_CODE = 30;
    public static final Integer QRCODE_WIDTH = 40;
    public static final Integer QRCODE_HEIGHT = 40;

    private Constants() {


    }
}
