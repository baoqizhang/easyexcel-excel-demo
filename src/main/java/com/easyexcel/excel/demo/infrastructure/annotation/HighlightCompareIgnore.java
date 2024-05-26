package com.easyexcel.excel.demo.infrastructure.annotation;

import java.lang.annotation.*;

/**
 * 下载被 recall 单子的 excel 时，如果字段与 recall 之前相比发生了变化，则会高亮发生变化的字段.
 * 此注解用来标注处理字段高亮时 不需要比较的字段，即 recall 之前和之后始终不变的字段，这种字段不高亮（但新增的sku会整行高亮）.
 * 此注解是为了避免多做 archive data 转换为 excel dto 时的数据查询，直接写死不高亮.
 *
 * @author Guang Lei
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HighlightCompareIgnore {}