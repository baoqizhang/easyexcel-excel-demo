package com.easyexcel.excel.demo.domain.logic;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.handler.WriteHandler;
import com.easyexcel.excel.demo.domain.model.DownloadParams;
import com.easyexcel.excel.demo.infrastructure.CustomCellLongestMatchColumnWithMinWidthStyleStrategy;
import com.easyexcel.excel.demo.infrastructure.CustomCellStyleStrategy;
import com.easyexcel.excel.demo.infrastructure.ExcelSheetDataV2;
import com.easyexcel.excel.demo.infrastructure.ExcelWriterContextV2;
import com.easyexcel.excel.demo.infrastructure.utils.ExcelUtil;
import jakarta.servlet.ServletOutputStream;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Component
public class AbstractDownloadLogic implements DownloadLogic{

    private static final String EXCEL_SHEET_NAME = "Sheet1";
    public void handleForGenerateExcel(List items, OutputStream outputStream) {
        var writer = EasyExcelFactory.write(outputStream).excelType(ExcelTypeEnum.XLSX).build();
        var sheetDataList = generateSheetData();
        ExcelUtil.writeExcelV2(writer, sheetDataList);
    }
    protected List<ExcelWriterContextV2> getWriterContexts(){return List.of();}

    protected Supplier<List<WriteHandler>> defaultCellWriteHandlerSupplier() {
        return () -> List.of(
                new CustomCellLongestMatchColumnWithMinWidthStyleStrategy(),
                new CustomCellStyleStrategy()
        );
    }

    @NonNull
    private List<ExcelSheetDataV2> generateSheetData() {
        return getWriterContexts().stream()
                .map(context -> convertContextToSheetData(context))
                .collect(toList());
    }
    private ExcelSheetDataV2 convertContextToSheetData(ExcelWriterContextV2 context) {
        List excelDtos = context.getConverter().convertToExcelRowDto();
        var highlightCells = context.getConverter().getHighlightCells(excelDtos);
        var sheetName = context.getSheetName();
        if (StringUtils.hasText(sheetName)) {
            sheetName = EXCEL_SHEET_NAME;
        }

        final var writeHandlers = Stream.of(
                        defaultCellWriteHandlerSupplier().get(),
                        context.getCustomerWriteHandlers()
                )
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(toList());

        return ExcelSheetDataV2.builder()
                .sheetName(sheetName)
                .itemClass(context.getItemClass())
                .dynamicHeaders(context.getDynamicHeaders())
                .items(excelDtos)
                .masterHeaderArgument(context.getMasterHeaderArgument())
                .highlightCells(highlightCells)
                .blankRows(context.getBlankRows())
                .writeHandlers(writeHandlers)
                .build();
    }

    @Override
    public boolean isSupport(DownloadParams params) {
        return false;
    }


    @Override
    public void afterDownload(List items) {
        // TODO
    }
}
