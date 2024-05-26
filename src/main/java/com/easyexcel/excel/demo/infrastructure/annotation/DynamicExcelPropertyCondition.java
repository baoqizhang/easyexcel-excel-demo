package com.easyexcel.excel.demo.infrastructure.annotation;

import org.springframework.lang.Nullable;

public interface DynamicExcelPropertyCondition {

    boolean isSatisfied(@Nullable Object args);
}
