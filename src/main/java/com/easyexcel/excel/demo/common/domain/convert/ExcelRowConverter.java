package com.easyexcel.excel.demo.domain.convert;


import com.easyexcel.excel.demo.infrastructure.CustomHighlightCellStyleStrategy.HighlightCell;

import java.util.List;

import static java.util.Collections.emptyList;

public interface ExcelRowConverterV2<T> {

    List<T> convertToExcelRowDto();

    default List<HighlightCell> getHighlightCells(List<T> dtos) {
        return emptyList();
    }
}
