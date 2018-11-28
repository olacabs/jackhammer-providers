package com.olacabs.jch.services.androscanner.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScanResult {
    @JsonIgnore
    private String package_version_code;
    @JsonIgnore
    private String analyze_mode;
    @JsonIgnore
    private String file_md5;
    @JsonIgnore
    private String apk_file_size;
    @JsonIgnore
    private String analyze_engine_build;
    @JsonIgnore
    private String package_version_name;
    @JsonIgnore
    private String minSdk;
    @JsonIgnore
    private String file_sha1;
    @JsonIgnore
    private String file_sha512;
    @JsonIgnore
    private String file_sha256;
    @JsonIgnore
    private String time_total;
    @JsonIgnore
    private String analyze_status;
    @JsonIgnore
    private String time_finish_analyze;
    @JsonIgnore
    private String package_name;
    @JsonIgnore
    private String time_starting_analyze;
    @JsonIgnore
    private String apk_filepath_absolute;
    @JsonIgnore
    private String vector_total_count;
    @JsonIgnore
    private String platform;
    @JsonIgnore
    private String time_loading_vm;
    @JsonIgnore
    private String time_analyze;
    @JsonIgnore
    private String targetSdk;

    private List<String> sensitive;

    private Map<String,FindingDetail> details;
}
