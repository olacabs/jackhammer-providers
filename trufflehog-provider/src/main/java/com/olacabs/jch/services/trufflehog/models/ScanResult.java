package com.olacabs.jch.services.trufflehog.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScanResult {
    private List<ParsedFinding> foundIssues;
}
