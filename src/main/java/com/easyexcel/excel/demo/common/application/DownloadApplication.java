package com.easyexcel.excel.demo.common.application;

import com.easyexcel.excel.demo.common.domain.enums.DownloadType;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;

import java.io.IOException;

public interface DownloadApplication {

    boolean isSupport(@NonNull DownloadType downloadType);

    void download(DownloadType type, @NonNull HttpServletResponse response) throws IOException;
}
