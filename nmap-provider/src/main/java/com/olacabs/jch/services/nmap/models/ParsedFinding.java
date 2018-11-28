package com.olacabs.jch.services.nmap.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParsedFinding {
    @JsonIgnore
    private String id;
    @JsonIgnore
    private String updated_at;
    @JsonIgnore
    private String created_at;
    @JsonIgnore
    private String publish_date;
    @JsonIgnore
    private String[] path;
    @JsonIgnore
    private String cvss_vector;

    private String title;
    private String overview;
    private String recommendation;
    private String cvss_score;
    private String module;
    private String version;
    private String vulnerable_versions;
    private String patched_versions;
    private String advisory;
}
