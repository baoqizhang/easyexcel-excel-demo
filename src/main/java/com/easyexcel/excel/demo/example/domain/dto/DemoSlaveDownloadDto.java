package com.easyexcel.excel.demo.example.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.easyexcel.excel.demo.example.domain.condition.DemoDynamicHeaderExcelPropertyCondition;
import com.easyexcel.excel.demo.infrastructure.annotation.DynamicExcelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DemoDynamicHeaderDownloadDto {
    @ExcelProperty("Demo Dynamic Header Name")
    private final String demoName;
    @DynamicExcelProperty(condition = DemoDynamicHeaderExcelPropertyCondition.class)
    @ExcelProperty("Demo Dynamic Header Status")
    private final String demoStatus;
}
