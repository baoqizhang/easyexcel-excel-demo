package com.easyexcel.excel.demo.infrastructure;

import com.alibaba.excel.write.handler.WriteHandler;
import com.easyexcel.excel.demo.common.domain.convert.ExcelRowConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

@Getter
@Setter
@Builder
public class ExcelWriterContext {

    @Nullable
    private String sheetName;
    private Class<?> itemClass;
    private List<String> dynamicHeaders;
    @NonNull
    private ExcelRowConverter<?> converter;
    @Nullable
    private Object masterHeaderArgument;
    @Nullable
    private int blankRows;
    @Nullable
    private List<? extends WriteHandler> customerWriteHandlers;
}