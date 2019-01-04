package com.olacabs.jch.services.bandit.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScanResult {
    @JsonIgnore
    private List<Map<String,String>> errors;
    @JsonIgnore
    private Map<String,Map<String,String>> metrics;
    @JsonIgnore
    private String generated_at;
    private List<ParsedFinding> results;
}
