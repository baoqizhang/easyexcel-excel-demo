package com.easyexcel.excel.demo.example.domain.logic;

import com.easyexcel.excel.demo.common.domain.enums.DownloadType;
import com.easyexcel.excel.demo.common.domain.logic.AbstractDownloadLogic;
import com.easyexcel.excel.demo.common.domain.model.DownloadParams;
import com.easyexcel.excel.demo.example.domain.convert.DemoMasterDownloadConvert;
import com.easyexcel.excel.demo.example.domain.convert.DemoSlaveDownloadConvert;
import com.easyexcel.excel.demo.example.domain.dto.DemoMasterDownloadDto;
import com.easyexcel.excel.demo.example.domain.dto.DemoSlaveDownloadDto;
import com.easyexcel.excel.demo.infrastructure.ExcelWriterContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DemoMasterDownloadLogic extends AbstractDownloadLogic {
    private final DemoMasterDownloadConvert masterDownloadConvert;
    private final DemoSlaveDownloadConvert slaveDownloadConvert;

    @Override
    public boolean isSupport(DownloadParams params) {
        return DownloadType.DEMO_MASTER_DOWNLOAD_TYPE == params.getDownloadType();
    }

    @Override
    public String getFileName() {
        return "Demo Master Test File.xlsx";
    }

    @Override
    protected List<ExcelWriterContext> getWriterContexts() {
        var masterContext = ExcelWriterContext.builder()
                .itemClass(DemoMasterDownloadDto.class)
                .blankRows(1)
                .converter(masterDownloadConvert)
                .build();

        var slaveContext = ExcelWriterContext.builder()
                .converter(slaveDownloadConvert)
                .itemClass(DemoSlaveDownloadDto.class)
                .build();

        return List.of(masterContext, slaveContext);
    }
}
