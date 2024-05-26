package com.easyexcel.excel.demo.example.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DemoDownloadDto {
    @ExcelProperty("Demo Name")
    private final String demoName;
    @ExcelProperty("Demo Status")
    private final String demoStatus;
}
