package com.olacabs.jch.services.androscanner.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindingDetail {
    private String count;
    private String title;
    private String vector_details;
    private String summary;
    private String level;
    private List<String> special_tag;
    private String cve_number;
}
