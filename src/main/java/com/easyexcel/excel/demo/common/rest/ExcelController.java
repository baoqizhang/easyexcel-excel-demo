package com.easyexcel.excel.demo.common.rest;

import com.easyexcel.excel.demo.common.application.DownloadApplicationComposite;
import com.easyexcel.excel.demo.common.domain.enums.DownloadType;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/download")
@RequiredArgsConstructor
public class ExcelController {
    private final DownloadApplicationComposite application;

    @GetMapping
    public void download(@RequestParam DownloadType downloadType, HttpServletResponse response) throws IOException {
        application.download(downloadType, response);
    }
}