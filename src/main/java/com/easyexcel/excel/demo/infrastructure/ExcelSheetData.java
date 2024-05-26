package com.easyexcel.excel.demo.infrastructure;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.handler.WriteHandler;
import com.easyexcel.excel.demo.infrastructure.annotation.DynamicExcelProperty;
import com.easyexcel.excel.demo.infrastructure.annotation.WithMasterHeaders;
import com.easyexcel.excel.demo.infrastructure.utils.ObjectUtil;
import com.easyexcel.excel.demo.infrastructure.utils.Pair;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.IntStream;

import static com.easyexcel.excel.demo.infrastructure.component.SpringApplicationContext.getStaticBean;
import static java.util.stream.Collectors.toList;


@Data
@Builder
public class ExcelSheetData<T> {

    private String sheetName;
    // static header class
    private Class<?> itemClass;
    // dynamic headers
    // 需要对列进行group，采用英文逗号分隔即可
    private List<String> dynamicHeaders;
    private Object masterHeaderArgument;
    private List<T> items;
    private List<CustomHighlightCellStyleStrategy.HighlightCell> highlightCells;
    private int blankRows;
    private List<WriteHandler> writeHandlers;

    public List<List<String>> getExcelDynamicHeaders() {
        return dynamicHeaders.stream()
                .map(it -> new ArrayList<>(List.of(it.split(","))))
                .collect(toList());
    }

    public List<List<Object>> getDynamicHeaderData() {
        var fields = getClassMappingHeaderFields(itemClass, dynamicHeaders);
        return items.stream()
                .map(it -> {
                    var value = new ArrayList<>();
                    fields.forEach(field -> {
                        ReflectionUtils.makeAccessible(field);
                        try {
                            value.add(field.get(it));
                        } catch (IllegalAccessException e) {
                            value.add("");
                        }
                    });
                    return value;
                })
                .collect(toList());
    }

    @NonNull
    public static List<Field> getClassMappingHeaderFields(Class<?> clazz, List<String> headers) {
        return ObjectUtil.getClassDeclaredFields(clazz).stream()
                .map(it -> Pair.of(it, it.getAnnotation(ExcelProperty.class)))
                .filter(it -> Objects.nonNull(it.getSecond()))
                .filter(it -> {
                    var header = Arrays.stream(it.getSecond().value()).findFirst().orElse("");
                    return headers.contains(header);
                })
                .sorted(Comparator.comparing(it -> it.getSecond().index()))
                .map(Pair::getFirst)
                .collect(toList());
    }

    public ExcelSheetData<T> updateSheetName(String sheetName) {
        this.sheetName = sheetName;
        return this;
    }

    public boolean isWithMasterHeaders() {
        if (itemClass == null) {
            return false;
        }
        return itemClass.getDeclaredAnnotation(WithMasterHeaders.class) != null;
    }

    public List<List<String>> getDefinedHeaders() {
        return getAllExcelHeaders(itemClass);
    }

    public List<List<String>> getHeaderWithMasterHeaders(List<List<String>> headers) {
        final var masterHeaders = getMasterHeaders();
        return mergeWithMasterHeaders(headers, masterHeaders,
                itemClass.getAnnotation(WithMasterHeaders.class).headers());
    }

    private List<List<String>> getAllExcelHeaders(Class<?> clazz) {
        var headers = new ArrayList<List<String>>();
        final var allFields = clazz.getDeclaredFields();
        for (Field field : allFields) {
            final var annotation = field.getDeclaredAnnotation(ExcelProperty.class);
            if (annotation != null) {
                final var annotationValues = annotation.value();
                headers.add(Arrays.asList(annotationValues));
            }
        }
        return headers;
    }

    private List<List<String>> getMasterHeaders() {
        var headers = new ArrayList<List<String>>();
        final var allFields = itemClass.getAnnotation(WithMasterHeaders.class).value().getDeclaredFields();
        for (Field field : allFields) {
            var conditionMapping = new HashMap<Class<?>, Boolean>(1);
            final var annotation = field.getDeclaredAnnotation(ExcelProperty.class);
            if (annotation != null) {
                final var annotationValues = annotation.value();

                var dynamicAnnotation = field.getAnnotation(DynamicExcelProperty.class);
                if (dynamicAnnotation == null) {
                    headers.add(Arrays.asList(annotationValues));
                    continue;
                }

                var conditionClazz = dynamicAnnotation.condition();
                if (conditionClazz == null) {
                    headers.add(Arrays.asList(annotationValues));
                    continue;
                }

                if (Boolean.TRUE.equals(conditionMapping.computeIfAbsent(conditionClazz,
                        it -> getStaticBean(conditionClazz).isSatisfied(this.masterHeaderArgument)))) {
                    headers.add(Arrays.asList(annotationValues));
                }
            }
        }
        return headers;
    }


    private static List<List<String>> mergeWithMasterHeaders(List<List<String>> to, List<List<String>> from,
                                                             String[] defaultHeader) {
        if (from.size() <= to.size()) {
            return to;
        }
        IntStream.range(0, from.size() - to.size())
                .forEach(it -> to.add(Arrays.asList(defaultHeader)));
        return to;
    }
}