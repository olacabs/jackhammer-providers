package com.olacabs.jch.services.dawnscanner.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScanResult {
	@JsonIgnore
    private String status;
	@JsonIgnore
	private long scan_started;
	@JsonIgnore
	private long scan_duration;
	@JsonIgnore
    private String dawn_version;
    @JsonIgnore
    private String target;
    @JsonIgnore
    private String mvc;
    @JsonIgnore
    private String mvc_version;
    @JsonIgnore
    private Integer applied_checks_count;
    @JsonIgnore
    private Integer skipped_checks_count;
    @JsonIgnore
    private Integer vulnerabilities_count;
    @JsonIgnore
    private Integer mitigated_issues_count;
    @JsonIgnore
    private Integer reflected_xss_count;
    @JsonIgnore
    private List<String> mitigated_vuln;
    @JsonIgnore
    private List<String> reflected_xss;
    private List<ParsedFinding> vulnerabilities;
}
