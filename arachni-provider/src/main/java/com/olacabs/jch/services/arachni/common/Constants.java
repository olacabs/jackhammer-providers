package com.olacabs.jch.services.arachni.common;

public class Constants {
    public static final String NOHUP = "nohup";
    public static final String SH = "sh";
    public static final String PROVIDER_NAME = "Arachni";
    public static final String COMPLETED_STATUS = "completed";
    public static final String FAILED_STATUS = "Failed";
    public static final String TEMP_FILE_PREFIX = "arachni";
    public static final String TEMP_FILE_SUFFIX = ".xml";
    public static final String AFR_TEMP_FILE_SUFFIX = ".afr";
    public static final String ARACHNI_CMD = "./arachni";
    public static final String ARACHNI_REPORTER_CMD = "./arachni_reporter";
    public static final String ARACHNI_REPORT_SAVE_ARG =  "--report-save-path";
    public static final String ARACHNI_REPORTER_OUT_FILE_ARG = "--reporter=xml:outfile=";
    public static final String COMMA_SEPARATOR = ",";


    //fields
    public static final String FINDING_TAG = "issue";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String SOLUTION = "remedy_guidance";
    public static final String SEVERITY = "severity";
    public static final String INFORMATIONAL = "Informational";
    public static final String INFO = "Info";
    public static final String CWE = "cwe";
    public static final String REFERENCE = "reference";
    public static final String URL = "url";
    public static final String REQUEST = "request";
    public static final String RESPONSE = "response";

    //pre deploy
    public static final String ARACHNI_INSTALLATION_PATH = "ARACHNI_INSTALLATION_PATH";
    public static final String ARACHNI_PRE_DEPLOY_SCRIPT_PATH = "ARACHNI_PRE_DEPLOY_SCRIPT_PATH";
    public static final String ARACHNI_PRE_DEPLOY_ERROR_LOG_PATH = "ARACHNI_PRE_DEPLOY_ERROR_LOG_PATH";
    public static final String ARACHNI_PRE_DEPLOY_OUTPUT_LOG_PATH = "ARACHNI_PRE_DEPLOY_OUTPUT_LOG_PATH";

    //deploy
    public static final String ARACHNI_DEPLOY_SCRIPT_PATH = "ARACHNI_DEPLOY_SCRIPT_PATH";
    public static final String ARACHNI_DEPLOY_ERROR_LOG_PATH = "ARACHNI_DEPLOY_ERROR_LOG_PATH";
    public static final String ARACHNI_DEPLOY_OUTPUT_LOG_PATH = "ARACHNI_DEPLOY_OUTPUT_LOG_PATH";


    //scan
    public static final String SCAN_ERROR_LOG_PATH = "SCAN_ERROR_LOG_PATH";
}
