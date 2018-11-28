package com.olacabs.jch.providers.brakeman.common;

public class Constants {

    public static final String NOHUP = "nohup";
    public static final String SH = "sh";
    public static final String PROVIDER_NAME = "Brakeman";
    public static final String TEMP_FILE_PREFIX = "brakeman";
    public static final String TEMP_FILE_SUFFIX = ".json";
    public static final String SCAN_FAIL = "Failed";
    public static final String BRAKEMAN_CMD = "brakeman";
    public static final String SCAN_COMPLETE = "Completed";
    public static final String WEAK_SEVERITY = "Weak";
    public static final String INFO_SEVERITY = "Info";

    //pre deploy
    public static final String BRAKEMAN_PRE_DEPLOY_SCRIPT_PATH = "BRAKEMAN_PRE_DEPLOY_SCRIPT_PATH";
    public static final String BRAKEMAN_PRE_DEPLOY_ERROR_LOG_PATH = "BRAKEMAN_PRE_DEPLOY_ERROR_LOG_PATH";
    public static final String BRAKEMAN_PRE_DEPLOY_OUTPUT_LOG_PATH = "BRAKEMAN_PRE_DEPLOY_OUTPUT_LOG_PATH";

    //deploy
    public static final String BRAKEMAN_DEPLOY_SCRIPT_PATH = "BRAKEMAN_DEPLOY_SCRIPT_PATH";
    public static final String BRAKEMAN_DEPLOY_ERROR_LOG_PATH = "BRAKEMAN_DEPLOY_ERROR_LOG_PATH";
    public static final String BRAKEMAN_DEPLOY_OUTPUT_LOG_PATH = "BRAKEMAN_DEPLOY_OUTPUT_LOG_PATH";

    //scan
    public static final String SCAN_ERROR_LOG_PATH = "SCAN_ERROR_LOG_PATH";
}
