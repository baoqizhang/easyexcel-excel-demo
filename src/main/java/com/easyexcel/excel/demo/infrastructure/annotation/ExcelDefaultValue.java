package com.sephora.nbp.common.download.domain.excel.annotation;

import com.sephora.nbp.infrastructure.enums.WorkflowType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelDefaultValue {
    String value();

    WorkflowType[] workflowTypes() default WorkflowType.ALL;
}

