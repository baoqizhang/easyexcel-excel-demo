package com.easyexcel.excel.demo.common.application;

import com.easyexcel.excel.demo.common.domain.logic.DownloadLogic;
import com.easyexcel.excel.demo.common.domain.model.DownloadParams;
import com.easyexcel.excel.demo.common.domain.enums.DownloadType;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.easyexcel.excel.demo.common.domain.logic.DownloadLogic.findAvailableDownloadLogic;
import static com.easyexcel.excel.demo.infrastructure.utils.ExcelUtil.*;

@Service
public abstract class AbstractDownloadApplication implements DownloadApplication {
    @Resource
    private List<DownloadLogic> downloadLogics;

    public void download(DownloadType type, HttpServletResponse response) throws IOException {
        var downloadLogic = findAvailableDownloadLogic(downloadLogics, DownloadParams.from(type));
        fillHttpServletResponseForExcel(downloadLogic.getFileName(), response);
        downloadLogic.handleForGenerateExcel(response.getOutputStream());
    }

    private void fillHttpServletResponseForExcel(String fileName, HttpServletResponse response) {
        response.setContentType(EXCEL_MEDIA_TYPE);
        response.setCharacterEncoding(CHARSET_UTF8);
        response.setHeader(CONTENT_DISPOSITION, "attachment;filename=\"" + fileName + "\"");
    }
}
