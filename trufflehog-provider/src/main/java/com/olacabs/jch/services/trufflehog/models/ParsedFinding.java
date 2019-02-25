package com.olacabs.jch.services.trufflehog.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParsedFinding {
    private String date;
    private String path;
    private String branch;
    private String commit;
    private String diff;
    private String commitHash;
    private List<String> stringsFound;
    private String reason;
    @JsonIgnore
    private String printDiff;
}
