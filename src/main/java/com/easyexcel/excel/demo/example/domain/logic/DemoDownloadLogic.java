package com.easyexcel.excel.demo.example.domain.logic;

import com.easyexcel.excel.demo.common.domain.logic.AbstractDownloadLogic;
import com.easyexcel.excel.demo.common.domain.model.DownloadParams;
import com.easyexcel.excel.demo.example.domain.convert.DemoDownloadConvert;
import com.easyexcel.excel.demo.example.domain.convert.DemoDynamicHeaderDownloadConvert;
import com.easyexcel.excel.demo.example.domain.dto.DemoDownloadDto;
import com.easyexcel.excel.demo.common.domain.enums.DownloadType;
import com.easyexcel.excel.demo.example.domain.dto.DemoDynamicHeaderDownloadDto;
import com.easyexcel.excel.demo.infrastructure.ExcelWriterContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.easyexcel.excel.demo.infrastructure.utils.ExcelUtil.getDynamicHeaders;

@Component
@RequiredArgsConstructor
public class DemoDownloadLogic extends AbstractDownloadLogic {

    private final DemoDownloadConvert demoDownloadConvert;
    private final DemoDynamicHeaderDownloadConvert demoDynamicHeaderDownloadConvert;
    private static final String SHEET_NAME = "Demo Download Sheet Name";

    @Override
    public boolean isSupport(DownloadParams params) {
        return DownloadType.DEMO_DOWNLOAD_TYPE == params.getDownloadType();
    }

    @Override
    public String getFileName() {
        return "Demo Test File.xlsx";
    }

    @Override
    protected List<ExcelWriterContext> getWriterContexts() {
        var demoDynamicHeaderContext = ExcelWriterContext.builder()
                .itemClass(DemoDynamicHeaderDownloadDto.class)
                .converter(demoDynamicHeaderDownloadConvert)
                .dynamicHeaders(getDynamicHeaders(DemoDynamicHeaderDownloadDto.class))
                .build();

        var demoContext = ExcelWriterContext.builder()
                .sheetName(SHEET_NAME)
                .converter(demoDownloadConvert)
                .itemClass(DemoDownloadDto.class)
                .build();


        return List.of(demoContext, demoDynamicHeaderContext);
    }
}
