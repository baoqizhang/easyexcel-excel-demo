package com.easyexcel.excel.demo.infrastructure;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomCellColumnWidthStrategy extends AbstractColumnWidthStyleStrategy {

    public static final Integer MAX_COLUMN_WIDTH = 255 * 256;
    public static final Integer MIN_COLUMN_WIDTH = 1100;
    private final Map<Integer, Map<Integer, Integer>> columnWidthMap = new HashMap<>();

    @Override
    public void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell,
        Head head, Integer relativeRowIndex, Boolean isHead) {
        boolean needSetWidth = isHead || !CollectionUtils.isEmpty(cellDataList);
        if (needSetWidth) {
            Map<Integer, Integer> maxColumnWidthMap = columnWidthMap.get(writeSheetHolder.getSheetNo());
            if (maxColumnWidthMap == null) {
                maxColumnWidthMap = new HashMap<>();
                columnWidthMap.put(writeSheetHolder.getSheetNo(), maxColumnWidthMap);
            }

            Integer contentLength = getDataLength(cellDataList, cell, isHead);
            if (contentLength >= 0) {
                if (contentLength > 254) {
                    contentLength = 254;
                }

                Integer maxColumnWidth = maxColumnWidthMap.get(cell.getColumnIndex());
                if (maxColumnWidth == null || contentLength > maxColumnWidth) {
                    maxColumnWidthMap.put(cell.getColumnIndex(), contentLength);
                    Sheet sheet = writeSheetHolder.getSheet();
                    Integer columnWidth = Math.min(contentLength * 320, MAX_COLUMN_WIDTH);
                    columnWidth = Math.max(columnWidth, MIN_COLUMN_WIDTH);
                    sheet.setColumnWidth(cell.getColumnIndex(), columnWidth);
                }
            }
        }
    }

    private Integer getDataLength(List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead) {
        if (isHead) {
            return cell.getStringCellValue().getBytes().length;
        } else {
            CellData cellData = cellDataList.get(0);
            CellDataTypeEnum type = cellData.getType();
            if (type == null) {
                return -1;
            } else {
                switch (type) {
                    case STRING:
                        int index = cellData.getStringValue().indexOf("\n");
                        return index != -1 ? cellData.getStringValue().substring(0, index).getBytes().length + 1
                            : cellData.getStringValue().getBytes().length + 1;
                    case BOOLEAN:
                        return cellData.getBooleanValue().toString().getBytes().length;
                    case NUMBER:
                        return cellData.getNumberValue().toString().getBytes().length;
                    default:
                        return -1;
                }
            }
        }
    }
}
