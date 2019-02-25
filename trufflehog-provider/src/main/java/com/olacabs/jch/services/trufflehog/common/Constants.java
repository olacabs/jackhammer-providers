package com.olacabs.jch.services.trufflehog.common;

public class Constants {
    public static final String NOHUP = "nohup";
    public static final String SH = "sh";
    public static final String PROVIDER_NAME = "TruffleHog";
    public static final String COMPLETED_STATUS = "Completed";
    public static final String FAILED_STATUS = "Failed";
    public static final String TEMP_FILE_PREFIX = "trufflehog";
    public static final String TEMP_FILE_SUFFIX = ".json";
    public static final String SCAN_FAIL = "Failed";
    public static final String PYTHON_CMD = "python";
    public static final String TRUFFLE_HOG_REPO_ARG = "--repo_path";
    public static final String OUTPUT_FILE_ARG = "--outputFile";
    public static final String HIGH = "High";
    public static final String TRUFFLE_HOG_SCAN_SCRIPT = "truffleHog/truffleHog.py";

    //PRE DEPLOY
    public static final String TRUFFLE_HOG_PRE_DEPLOY_SCRIPT_PATH = "TRUFFLE_HOG_PRE_DEPLOY_SCRIPT_PATH";
    public static final String TRUFFLE_HOG_PRE_DEPLOY_ERROR_LOG_PATH = "TRUFFLE_HOG_PRE_DEPLOY_ERROR_LOG_PATH";
    public static final String TRUFFLE_HOG_PRE_DEPLOY_OUTPUT_LOG_PATH = "TRUFFLE_HOG_PRE_DEPLOY_OUTPUT_LOG_PATH";

    //DEPLOY
    public static final String TRUFFLE_HOG_DEPLOY_SCRIPT_PATH = "TRUFFLE_HOG_DEPLOY_SCRIPT_PATH";
    public static final String TRUFFLE_HOG_DEPLOY_ERROR_LOG_PATH = "TRUFFLE_HOG_DEPLOY_ERROR_LOG_PATH";
    public static final String TRUFFLE_HOG_DEPLOY_OUTPUT_LOG_PATH = "TRUFFLE_HOG_DEPLOY_OUTPUT_LOG_PATH";
    //installation
    public static final String TRUFFLE_HOG_INSTALLED_PATH = "TRUFFLE_HOG_INSTALLED_PATH";
    //scan
    public static final String TRUFFLE_HOG_ERROR_LOG_PATH = "TRUFFLE_HOG_ERROR_LOG_PATH";
    public static final String TRUFFLE_HOG_OUT_PUT_LOG_PATH = "TRUFFLE_HOG_OUT_PUT_LOG_PATH";
}
