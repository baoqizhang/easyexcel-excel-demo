package com.sephora.nbp.common.download.domain.logic.condition;

import org.springframework.lang.Nullable;

public interface DynamicExcelPropertyCondition {

    boolean isSatisfied(@Nullable Object args);
}
