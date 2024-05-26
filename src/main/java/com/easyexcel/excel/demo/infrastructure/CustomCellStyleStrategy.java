package com.easyexcel.excel.demo.infrastructure;

import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.AbstractCellStyleStrategy;
import org.apache.poi.ss.usermodel.VerticalAlignment;

public class CustomCellStyleStrategy extends AbstractCellStyleStrategy {

    @Override
    protected void setHeadCellStyle(CellWriteHandlerContext context) {
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setBold(true);
        headWriteFont.setFontName("Arial");
        headWriteFont.setFontHeightInPoints((short) 11);
        WriteCellStyle style = new WriteCellStyle();
        style.setWriteFont(headWriteFont);
        WriteCellStyle.merge(style, context.getFirstCellData().getOrCreateStyle());
    }

    @Override
    protected void setContentCellStyle(CellWriteHandlerContext context) {
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontName("Arial");
        WriteCellStyle style = new WriteCellStyle();
        style.setWriteFont(headWriteFont);
        style.setWrapped(true);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        WriteCellStyle.merge(style, context.getFirstCellData().getOrCreateStyle());
    }
}
