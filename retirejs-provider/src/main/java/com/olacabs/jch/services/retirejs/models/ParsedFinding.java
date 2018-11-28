package com.olacabs.jch.services.retirejs.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParsedFinding {
   private   String file;
    private List<FindingResult> results;

}
