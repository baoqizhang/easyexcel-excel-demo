package com.easyexcel.excel.demo.common.application;

import com.easyexcel.excel.demo.common.domain.logic.DownloadLogic;
import com.easyexcel.excel.demo.common.domain.model.DownloadParams;
import com.easyexcel.excel.demo.example.domain.enums.DemoDownloadType;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.easyexcel.excel.demo.common.domain.logic.DownloadLogic.findAvailableDownloadLogic;

@Service
@RequiredArgsConstructor
public abstract class AbstractDownloadApplication {
    private final List<DownloadLogic> downloadLogics;
    public void download(HttpServletResponse response) throws IOException {
        var params = DownloadParams.from(DemoDownloadType.DEMO_DOWNLOAD_TYPE);
        var downloadLogic = findAvailableDownloadLogic(downloadLogics, params);
        downloadLogic.handleForGenerateExcel(response.getOutputStream());
    }
}
