package com.easyexcel.excel.demo.example.domain.convert;

import com.easyexcel.excel.demo.common.domain.convert.ExcelRowConverter;
import com.easyexcel.excel.demo.example.domain.dto.DemoDownloadDto;
import com.easyexcel.excel.demo.example.domain.dto.DemoMasterDownloadDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DemoMasterDownloadConvert implements ExcelRowConverter<DemoMasterDownloadDto> {
    @Override
    public List<DemoMasterDownloadDto> convertToExcelRowDto() {
        return List.of(DemoMasterDownloadDto.builder()
                .demoName("demo name")
                .demoStatus("open")
                .demoTest("test")
                .build());
    }
}
