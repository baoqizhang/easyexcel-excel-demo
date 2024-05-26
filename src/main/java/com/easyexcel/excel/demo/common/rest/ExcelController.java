package com.easyexcel.excel.demo.rest;

import com.easyexcel.excel.demo.application.DownloadApplication;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/download")
@RequiredArgsConstructor
public class ExcelController {
    private final DownloadApplication application;

    @GetMapping
    public void download(HttpServletResponse response) throws IOException {
        application.download(response);
    }

}