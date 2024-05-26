package com.easyexcel.excel.demo.common.application;

import com.easyexcel.excel.demo.common.domain.enums.DownloadType;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@RequiredArgsConstructor
@Service
public class DownloadApplicationComposite {

    private final List<DownloadApplication> applications;

    public void download(DownloadType type, HttpServletResponse response) throws IOException {
        var downloadApplication = findAvailableDownloadApplication(type);
        downloadApplication.download(type, response);
    }

    private DownloadApplication findAvailableDownloadApplication(DownloadType type) {
        return applications.stream()
                .filter(it -> it.isSupport(type))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(type + " download application not implemented."));
    }
}
