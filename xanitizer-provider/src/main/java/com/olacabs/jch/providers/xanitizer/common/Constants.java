package com.olacabs.jch.providers.xanitizer.common;

public class Constants {
    public static final String PROVIDER_NAME = "Xanitizer";
    public static final String NOHUP="nohup";
    public static final String SH = "sh";
    public static final String TEMP_FILE_PREFIX = "xanitizer";
    public static final String TEMP_FILE_SUFFIX = ".xml";
    public static final String COMPLETED_STATUS = "Completed";
    public static final String FAILED_STATUS = "Failed";
    public static final String CRITICAL = "Critical";
    public static final String HIGH = "High";
    public static final String MEDIUM = "Medium";
    public static final String LOW = "Low";
    public static final String INFO = "Info";
    public static final String CLI_SCRIPT_COMMAND = "./XanitizerHeadless";
    public static final String ROOT_DIRECTORY = "rootDirectory=";
    public static final String FINDINGS_LIST_REPORT_OUTPUT_FILE = "findingsListReportOutputFile=";
    public static final String LOG_LEVEL = "XanitizerLogLevel=OFF";
    public static final String CREATE_SNAPSHOT = "createSnapshot=false";
    public static final String XANITIZER_FINDING = "finding";



    // deploy
    public static final String XANITIZER_DEPLOY_SCRIPT_PATH = "XANITIZER_DEPLOY_SCRIPT_PATH";
    public static final String XANITIZER_DEPLOY_ERROR_LOG_PATH = "XANITIZER_DEPLOY_ERROR_LOG_PATH";
    public static final String XANITIZER_DEPLOY_OUTPUT_LOG_PATH = "XANITIZER_DEPLOY_OUTPUT_LOG_PATH";

    // post deploy
    public static final String XANITIZER_POST_DEPLOY_SCRIPT_PATH = "XANITIZER_POST_DEPLOY_SCRIPT_PATH";
    public static final String XANITIZER_POST_DEPLOY_ERROR_LOG_PATH = "XANITIZER_POST_DEPLOY_ERROR_LOG_PATH";
    public static final String XANITIZER_POST_DEPLOY_OUTPUT_LOG_PATH = "XANITIZER_POST_DEPLOY_OUTPUT_LOG_PATH";


    //scan
    public static final String XANITIZER_INSTALLED_PATH = "XANITIZER_INSTALLED_PATH";
    public static final String SCAN_ERROR_LOG_PATH = "SCAN_ERROR_LOG_PATH";

    //xml attributes
    public static final String PROBLEM_TYPE = "problemType";
    public static final String DESCRIPTION = "description";
    public static final String LINE = "line";
    public static final String FILE = "file";
    public static final String CLASS = "class";
    public static final String RATING = "rating";
    public static final String CWE_NUMBER = "cweNumber";
}
