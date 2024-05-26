package com.easyexcel.excel.demo.example.domain.condition;

import com.easyexcel.excel.demo.infrastructure.annotation.DynamicExcelPropertyCondition;
import org.springframework.stereotype.Component;

@Component
public class DemoDynamicHeaderExcelPropertyCondition implements DynamicExcelPropertyCondition {
    @Override
    public boolean isSatisfied(Object args) {
        return false;
    }
}
