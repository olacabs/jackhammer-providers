package com.olacabs.jch.providers.brakeman.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.olacabs.jch.sdk.models.ScanResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScanResult extends ScanResponse {
    @JsonIgnore
    private HashMap<String,HashMap<String,String>> scan_info;
    @JsonIgnore
    private List<String> ignored_warnings;
    @JsonIgnore
    private List<String> errors;
    @JsonIgnore
    private List<String> obsolete;
    private List<ParsedFinding> warnings;
}
