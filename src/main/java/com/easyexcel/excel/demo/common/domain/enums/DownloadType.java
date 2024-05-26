package com.easyexcel.excel.demo.example.domain.enums;

import com.easyexcel.excel.demo.common.domain.model.DownloadType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DemoDownloadType implements DownloadType {
    DEMO_DOWNLOAD_TYPE("demoDownloadType");

    private final String prefixFileName;
}
