package com.easyexcel.excel.demo.example.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.easyexcel.excel.demo.infrastructure.annotation.WithMasterHeaders;
import lombok.Builder;
import lombok.Getter;

import static com.easyexcel.excel.demo.example.domain.dto.DemoMasterDownloadDto.TITLE_ROW;

@Getter
@Builder
@WithMasterHeaders(value = DemoSlaveDownloadDto.class, headers = {TITLE_ROW, ""})
public class DemoMasterDownloadDto {
    static final String TITLE_ROW = "Test";
    @ExcelProperty({TITLE_ROW, "Demo Name"})
    private final String demoName;
    @ExcelProperty({TITLE_ROW, "Demo Status"})
    private final String demoStatus;
    @ExcelProperty({TITLE_ROW, "Demo Test"})
    private final String demoTest;
}
