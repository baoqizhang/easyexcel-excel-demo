package com.easyexcel.excel.demo.infrastructure.utils;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.handler.WriteHandler;
import com.easyexcel.excel.demo.infrastructure.ExcelSheetData;
import com.easyexcel.excel.demo.infrastructure.annotation.DynamicExcelProperty;
import com.easyexcel.excel.demo.infrastructure.annotation.ExcelDefaultValue;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.easyexcel.excel.demo.infrastructure.component.SpringApplicationContext.getStaticBean;

public class ExcelUtil {

    public static final String CONTENT_DISPOSITION = "Content-disposition";
    public static final String EXCEL_MEDIA_TYPE = "application/vnd.ms-excel";
    public static final String CHARSET_UTF8 = "utf-8";
    public static final String URL_PLUS = "+";
    public static final String URL_ENCODED_PLUS = "%2B";
    public static final String URL_ENCODED_SPACE = "%20";
    public static final Integer ONE_SIZE = 1;

    private ExcelUtil() {
    }

    @SuppressWarnings("rawtypes")
    public static void writeExcelV2(ExcelWriter writer, List<ExcelSheetData> dataList) {
        if (dataList == null) {
            return;
        }
        writeDataToExcel(writer, dataList);
        writer.finish();
    }

    @SuppressWarnings("rawtypes")
    public static File generateExcelFile(String fileName, List<ExcelSheetData> dataList) {
        var file = new File(fileName);
        var writer = EasyExcelFactory.write(file)
            .excelType(ExcelTypeEnum.XLSX)
//            .registerWriteHandler(new CustomCellColumnWidthStrategy())
//            .registerWriteHandler(new CustomCellStyleStrategy())
            .build();
        writeExcelV2(writer, dataList);
        return file;
    }

    public static <T> T setDefaultValue(T item) {
        if (Objects.isNull(item)) {
            return null;
        }
        Class<?> dtoClass = item.getClass();
        for (Field field : dtoClass.getDeclaredFields()) {
            ReflectionUtils.makeAccessible(field);
            ExcelDefaultValue excelDefaultValue = field.getAnnotation(ExcelDefaultValue.class);
            if (excelDefaultValue == null) {
                continue;
            }

                Object value = ReflectionUtils.getField(field, item);
                String stringValue = (String) value;
                if (StringUtils.isBlank(stringValue)) {
                    ReflectionUtils.setField(field, item, excelDefaultValue.value());
                }
        }
        return item;
    }

    @SuppressWarnings("rawtypes")
    public static ExcelWriter writeExcelForZip(ExcelWriter writer, List<ExcelSheetData> dataList) {
        if (dataList == null) {
            return null;
        }
        writeDataToExcel(writer, dataList);
        return writer;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void writeDataToExcel(ExcelWriter writer, List<ExcelSheetData> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            final var data = dataList.get(i);
            final var sheetName = data.getSheetName();
            final var sheet = EasyExcelFactory.writerSheet().sheetName(sheetName).build();
            final var tableBuilder = EasyExcelFactory.writerTable(i);
            final var isDynamicHeaders = !CollectionUtils.isEmpty(data.getDynamicHeaders());
            final var isMasterHeaders = data.isWithMasterHeaders();
            if (isDynamicHeaders && isMasterHeaders) {
                tableBuilder.head(data.getHeaderWithMasterHeaders(data.getExcelDynamicHeaders()));
            }
            if (!isDynamicHeaders && isMasterHeaders) {
                tableBuilder.head(data.getHeaderWithMasterHeaders(data.getDefinedHeaders()));
            }
            if (isDynamicHeaders && !isMasterHeaders) {
                tableBuilder.head(data.getExcelDynamicHeaders());
            }
            if (!isDynamicHeaders && !isMasterHeaders) {
                tableBuilder.head(data.getItemClass());
            }
            if (!CollectionUtils.isEmpty(data.getWriteHandlers())) {
                data.getWriteHandlers().forEach(it -> tableBuilder.registerWriteHandler((WriteHandler) it));
            }
//            tableBuilder.registerWriteHandler(new CustomHighlightCellStyleStrategy(data.getHighlightCells())).build();
            final var writingData = new ArrayList<>(
                isDynamicHeaders && data.getItemClass() != null ? data.getDynamicHeaderData() : data.getItems());
            IntStream.range(0, data.getBlankRows()).forEach(c -> writingData.add(List.of()));
            writer.write(writingData, sheet, tableBuilder.build());
        }
    }

    public static List<String> getDynamicHeaders(Class<?> clazz) {
        return getDynamicHeaders(clazz, null);
    }

    public static List<String> getDynamicHeaders(Class<?> clazz, @Nullable Object conditionArgs) {
        var conditionMapping = new HashMap<Class<?>, Boolean>(1);
        Predicate<Field> isDynamicProperty = field -> {
            if (field.getAnnotation(ExcelProperty.class) == null) {
                return false;
            }
            var annotation = field.getAnnotation(DynamicExcelProperty.class);
            if (annotation == null) {
                return true;
            }
            var conditionClazz = annotation.condition();
            if (conditionClazz == null) {
                return true;
            }
            return conditionMapping.computeIfAbsent(conditionClazz,
                it -> getStaticBean(conditionClazz).isSatisfied(conditionArgs));
        };
        return ObjectUtil.getClassDeclaredFields(clazz).stream()
            .filter(isDynamicProperty)
            .map(it -> it.getAnnotation(ExcelProperty.class))
            .sorted(Comparator.comparing(ExcelProperty::index))
            .map(it -> Arrays.stream(it.value()).findFirst().orElse(""))
            .collect(Collectors.toList());
    }
}