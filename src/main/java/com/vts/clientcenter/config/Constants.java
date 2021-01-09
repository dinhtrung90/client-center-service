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

    /**
     * Name of the job
     */
    public static final String JOB_NAME = "employee-batch-loader";

    /**
     * Name of the step
     */
    public static final String STEP_NAME = "process-employees-step";

    /**
     * Job parameter name for job file name
     */
    public static final String JOB_PARAM_FILE_NAME = "employee-batch-loader.fileName";

    /**
     * Item reader name for the job
     */
    public static final String ITEM_READER_NAME = "employee-item-reader";

    public static final String SPRING_PROFILE_NO_LIQUIBASE = "no-liquibase";

    private Constants() {}
}
