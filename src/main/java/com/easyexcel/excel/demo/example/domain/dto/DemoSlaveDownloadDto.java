package com.easyexcel.excel.demo.example.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DemoSlaveDownloadDto {
    @ExcelProperty("Demo Dynamic Header Name")
    private final String demoName;
    @ExcelProperty("Demo Dynamic Header Status")
    private final String demoStatus;
}
