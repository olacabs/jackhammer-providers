package com.olacabs.jch.services.bandit.common;

public class Constants {
    public static final String NOHUP = "nohup";
    public static final String SH = "sh";
    public static final String PROVIDER_NAME = "Bandit";
    public static final String SUCCESS_STATUS = "Success";
    public static final String FAILED_STATUS = "Failed";
    public static final String COMPLETED_STATUS = "Completed";
    public static final String TEMP_FILE_PREFIX = "bandit";
    public static final String TEMP_FILE_SUFFIX = ".json";
    public static final String BANDIT_CMD = "bandit";
    public static final String WRITE_REPORT_TO_FILENAME_CMD = "--output";
    public static final String OUTPUT_FILE_FORMAT_CMD = "--format";
    public static final String OUTPUT_FILE_FORMAT = "json";
    public static final String FIND_AND_PROCESS_FILES_IN_SUBDIRECTORIES = "-r";


    //pre deploy
    public static final String BANDIT_PRE_DEPLOY_SCRIPT_PATH = "BANDIT_PRE_DEPLOY_SCRIPT_PATH";
    public static final String BANDIT_PRE_DEPLOY_ERROR_LOG_PATH = "BANDIT_PRE_DEPLOY_ERROR_LOG_PATH";
    public static final String BANDIT_PRE_DEPLOY_OUTPUT_LOG_PATH = "BANDIT_PRE_DEPLOY_OUTPUT_LOG_PATH";

    //deploy
    public static final String BANDIT_DEPLOY_SCRIPT_PATH = "BANDIT_DEPLOY_SCRIPT_PATH";
    public static final String BANDIT_DEPLOY_ERROR_LOG_PATH = "BANDIT_DEPLOY_ERROR_LOG_PATH";
    public static final String BANDIT_DEPLOY_OUTPUT_LOG_PATH = "BANDIT_DEPLOY_OUTPUT_LOG_PATH";

    //scan
    public static final String SCAN_ERROR_LOG_PATH = "SCAN_ERROR_LOG_PATH";
}
