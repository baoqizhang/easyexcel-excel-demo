package com.easyexcel.excel.demo.infrastructure.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelDefaultValue {
    String value();

}

