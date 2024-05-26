package com.easyexcel.excel.demo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
public class DownloadParams {

    @Nullable
    private DownloadType downloadType;

    @Override
    public String toString() {
        return "DownloadParams{ downloadType=" + downloadType + "}";
    }
}
