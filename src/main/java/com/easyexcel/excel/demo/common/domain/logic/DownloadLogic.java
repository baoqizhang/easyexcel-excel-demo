package com.easyexcel.excel.demo.domain.logic;

import com.alibaba.excel.ExcelWriter;
import com.easyexcel.excel.demo.domain.model.DownloadParams;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.lang.NonNull;

import java.io.File;
import java.io.OutputStream;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public interface DownloadLogic<T> {
    boolean isSupport(@NonNull DownloadParams params);

    void handleForGenerateExcel(List<T> items, OutputStream outputStream);

    void afterDownload(List<T> items);

    static DownloadLogic findAvailableDownloadLogic(List<DownloadLogic> downloadLogics,
                                                    DownloadParams params) {
        return downloadLogics.stream()
                .filter(item -> item.isSupport(params))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(params + " download logic not implemented."));
    }
}
