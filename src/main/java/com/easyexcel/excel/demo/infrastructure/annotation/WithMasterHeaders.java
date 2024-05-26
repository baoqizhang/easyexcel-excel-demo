package com.easyexcel.excel.demo.infrastructure.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WithMasterHeaders {

    Class<?> value();

    String[] headers() default {};
}
