package com.easyexcel.excel.demo.example.domain.convert;

import com.easyexcel.excel.demo.common.domain.convert.ExcelRowConverter;
import com.easyexcel.excel.demo.example.domain.dto.DemoDownloadDto;
import com.easyexcel.excel.demo.example.domain.dto.DemoSlaveDownloadDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DemoSlaveDownloadConvert implements ExcelRowConverter<DemoSlaveDownloadDto> {
    @Override
    public List<DemoSlaveDownloadDto> convertToExcelRowDto() {
        return List.of(DemoSlaveDownloadDto.builder()
                .demoName("demo name")
                .demoStatus("open")
                .build());
    }
}
