package com.easyexcel.excel.demo.example.domain.convert;

import com.easyexcel.excel.demo.common.domain.convert.ExcelRowConverter;
import com.easyexcel.excel.demo.example.domain.dto.DemoDownloadDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DemoMasterDownloadConvert implements ExcelRowConverter<DemoDownloadDto> {
    @Override
    public List<DemoDownloadDto> convertToExcelRowDto() {
        return List.of(DemoDownloadDto.builder()
                .demoName("demo name")
                .demoStatus("open")
                .build());
    }
}
