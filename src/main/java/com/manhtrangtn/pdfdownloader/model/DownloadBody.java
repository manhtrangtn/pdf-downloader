package com.manhtrangtn.pdfdownloader.model;

import lombok.Data;

import java.util.Map;

@Data
public class DownloadBody {
    private String url;
    private String docId;
    private Integer numberOfPages;
    private String subFolder;
}
