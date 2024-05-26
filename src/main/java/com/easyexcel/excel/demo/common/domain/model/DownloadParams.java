package com.easyexcel.excel.demo.common.domain.model;

import com.easyexcel.excel.demo.common.domain.enums.DownloadType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
public class DownloadParams {

    @Nullable
    private DownloadType downloadType;

    public static DownloadParams from(DownloadType downloadType) {
        return new DownloadParams(downloadType);
    }

    @Override
    public String toString() {
        return "DownloadParams{ downloadType=" + downloadType + "}";
    }
}
