package com.sephora.nbp.common.download.domain.excel.style;

import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.AbstractCellStyleStrategy;
import lombok.Getter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.util.CollectionUtils;
import java.util.List;

public class CustomHighlightCellStyleStrategy extends AbstractCellStyleStrategy {

    private final List<HighlightCell> highlightCells;

    public CustomHighlightCellStyleStrategy(List<HighlightCell> highlightCells) {
        this.highlightCells = highlightCells;
    }

    @Override
    protected void setHeadCellStyle(CellWriteHandlerContext context) {}

    @Override
    protected void setContentCellStyle(CellWriteHandlerContext context) {
        if (CollectionUtils.isEmpty(highlightCells)) {
            return;
        }
        highlightCells.stream()
            .filter(it -> it.getRowIndex().equals(context.getRowIndex())
                && it.getColumnIndex().equals(context.getColumnIndex()))
            .findFirst()
            .ifPresent(highlightCell -> {
                var style = new WriteCellStyle();
                style.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
                style.setFillForegroundColor(highlightCell.backgroundColor.getIndex());
                WriteCellStyle.merge(style, context.getFirstCellData().getOrCreateStyle());
            });
    }

    @Getter
    public static class HighlightCell {

        private final Integer rowIndex;
        private final Integer columnIndex;
        private final IndexedColors backgroundColor;

        public HighlightCell(Integer rowIndex, Integer columnIndex, IndexedColors backgroundColor) {
            this.rowIndex = rowIndex;
            this.columnIndex = columnIndex;
            this.backgroundColor = backgroundColor;
        }
    }
}
