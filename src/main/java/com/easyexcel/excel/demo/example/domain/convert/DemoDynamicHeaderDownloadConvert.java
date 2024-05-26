package com.easyexcel.excel.demo.example.domain.convert;

import ch.qos.logback.core.rolling.helper.DateTokenConverter;
import com.easyexcel.excel.demo.common.domain.convert.ExcelRowConverter;
import com.easyexcel.excel.demo.example.domain.dto.DemoDynamicHeaderDownloadDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DemoDynamicHeaderDownloadConvert implements ExcelRowConverter<DemoDynamicHeaderDownloadDto> {
    @Override
    public List<DemoDynamicHeaderDownloadDto> convertToExcelRowDto() {
        return List.of(
                DemoDynamicHeaderDownloadDto.builder()
                        .demoName("Demo Dynamic Header Download Dto Name")
                        .demoStatus("Demo Dynamic Header Download Dto Status")
                        .build()
        );
    }
}
