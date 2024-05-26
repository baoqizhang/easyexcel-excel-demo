package com.easyexcel.excel.demo.common.domain.logic;

import com.easyexcel.excel.demo.common.domain.model.DownloadParams;
import org.springframework.lang.NonNull;

import java.io.OutputStream;
import java.util.List;

public interface DownloadLogic<T> {
    boolean isSupport(@NonNull DownloadParams params);

    void handleForGenerateExcel(OutputStream outputStream);

    void afterDownload(List<T> items);

    String getFileName();

    static DownloadLogic findAvailableDownloadLogic(List<DownloadLogic> downloadLogics,
                                                    DownloadParams params) {
        return downloadLogics.stream()
                .filter(item -> item.isSupport(params))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(params + " download logic not implemented."));
    }
}
