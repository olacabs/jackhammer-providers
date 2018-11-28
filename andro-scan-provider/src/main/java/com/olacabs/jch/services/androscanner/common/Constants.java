package com.olacabs.jch.services.androscanner.common;

public class Constants {
    public static final String NOHUP = "nohup";
    public static final String SH = "sh";
    public static final String PROVIDER_NAME = "AndroScanner";
    public static final String COMPLETED_STATUS = "Completed";
    public static final String FAILED_STATUS = "Failed";
    public static final String TEMP_FILE_PREFIX = "andro";
    public static final String TEMP_FILE_SUFFIX = ".json";
    public static final String SCAN_FAIL = "Failed";
    public static final String PYTHON_CMD = "python";
    public static final String ANDRO_E_ARG = "-e";
    public static final String ANDRO_TWO_ARG = "2";
    public static final String ANDRO_F_ARG = "-f";
    public static final String ANDRO_J_ARG = "-j";
    public static final String HIGH = "High";
    public static final String LOW = "Low";
    public static final String WARNING = "Warning";
    public static final String NOTICE = "Notice";
    public static final String INFO = "Info";
    public static final String ANDRO_SCAN_SCRIPT = "mandrobugs.py";

    //Sensitive data finding
    public static final String FINDING_TITLE = "Sensitive data";
    public static final String FINDING_DESCRIPTION = "These are the sensitive strings hardcoded in the APK";
    public static final String FINDING_SOLUTION = "Review all the hardcoded values in the APK and remove the sensitive one from the APK";



    //pre deploy
    public static final String ANDRO_SCANNER_PRE_DEPLOY_SCRIPT_PATH = "ANDRO_SCANNER_PRE_DEPLOY_SCRIPT_PATH";
    public static final String ANDRO_SCANNER_PRE_DEPLOY_ERROR_LOG_PATH = "ANDRO_SCANNER_PRE_DEPLOY_ERROR_LOG_PATH";
    public static final String ANDRO_SCANNER_PRE_DEPLOY_OUTPUT_LOG_PATH = "ANDRO_SCANNER_PRE_DEPLOY_OUTPUT_LOG_PATH";

    //deploy
    public static final String ANDRO_SCANNER_DEPLOY_SCRIPT_PATH = "ANDRO_SCANNER_DEPLOY_SCRIPT_PATH";
    public static final String ANDRO_SCANNER_DEPLOY_ERROR_LOG_PATH = "ANDRO_SCANNER_DEPLOY_ERROR_LOG_PATH";
    public static final String ANDRO_SCANNER_DEPLOY_OUTPUT_LOG_PATH = "ANDRO_SCANNER_DEPLOY_OUTPUT_LOG_PATH";


    //scan
    public static final String ANDRO_SCANNER_ERROR_LOG_PATH = "ANDRO_SCANNER_ERROR_LOG_PATH";
    public static final String ANDRO_SCANNER_INSTALLATION_PATH = "ANDRO_SCANNER_INSTALLATION_PATH";
}
