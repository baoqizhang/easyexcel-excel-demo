package com.easyexcel.excel.demo.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Slf4j
public class ObjectUtil {

    private ObjectUtil() {
    }

    public static String toJson(Object source) {
        var objectMapper = SpringApplicationContext.getStaticBean(ObjectMapper.class);
        try {
            return objectMapper.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            log.error("Object convert json string error", e);
            return null;
//            throw new GenericBizException(HttpStatus.BAD_REQUEST, "Object convert json data error, " + e.getMessage());
        }
    }

    public static <T> T convert(Class<T> target, Object source) {
        var objectMapper = SpringApplicationContext.getStaticBean(ObjectMapper.class);
        try {
            var json = objectMapper.writeValueAsString(source);
            return objectMapper.readValue(json, target);
        } catch (Exception e) {
            log.error("Object convert to class obj error", e);
            return null;
//            throw new Exception(HttpStatus.BAD_REQUEST, "Object convert error, " + e.getMessage());
        }
    }

    public static <T> T convert(Class<T> target, String json) {
        var objectMapper = SpringApplicationContext.getStaticBean(ObjectMapper.class);
        try {
            return objectMapper.readValue(json, target);
        } catch (Exception e) {
            log.error("String convert to class obj error", e);
            return null;
//            throw new GenericBizException(HttpStatus.BAD_REQUEST, "Object convert error, " + e.getMessage());
        }
    }

    public static <T> List<T> convertToList(Class<T> target, String json) {
        var objectMapper = SpringApplicationContext.getStaticBean(ObjectMapper.class);
        List<T> objList;
        try {
            JavaType t = objectMapper.getTypeFactory().constructParametricType(
                List.class, target);
            objList = objectMapper.readValue(json, t);
        } catch (Exception e) {
            log.error("String convert to list error", e);
            return null;
//            throw new GenericBizException(HttpStatus.BAD_REQUEST, "Object convert error, " + e.getMessage());
        }
        return objList;
    }

    public static JsonNode convertToJsonNode(InputStream inputStream) {
        var objectMapper = SpringApplicationContext.getStaticBean(ObjectMapper.class);
        try {
            var jsonNode = objectMapper.readTree(inputStream);
            inputStream.close();
            return jsonNode;
        } catch (Exception e) {
            log.error("Import product classification hierarchy failed", e);
            return null;
//            throw new GenericBizException("Can't convert inputStream to JsonNode" + e.getMessage());
        }
    }

    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        try {
            var targetFields = getClassAllDeclaredFields(target.getClass());
            for (Field targetField : targetFields) {
                ReflectionUtils.makeAccessible(targetField);
                var sourceField = ReflectionUtils.findField(source.getClass(), targetField.getName());
                if (sourceField == null || Arrays.stream(ignoreProperties)
                    .anyMatch(property -> Objects.equals(property, sourceField.getName()))) {
                    continue;
                }
                ReflectionUtils.makeAccessible(sourceField);
                var value = ReflectionUtils.getField(sourceField, source);
                ReflectionUtils.setField(targetField, target, value);
            }
        } catch (Exception e) {
            log.error("Copy source properties to target obj error", e);
//            throw new GenericBizException(HttpStatus.BAD_REQUEST, "Copy properties failure, " + e.getMessage());
        }
    }

    public static <S, T> T copyProperties(S source, T target) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        try {
            var targetFields = getClassAllDeclaredFields(target.getClass());
            for (Field targetField : targetFields) {
                ReflectionUtils.makeAccessible(targetField);
                var sourceField = ReflectionUtils.findField(source.getClass(), targetField.getName());
                if (sourceField == null) {
                    continue;
                }
                ReflectionUtils.makeAccessible(sourceField);
                var value = ReflectionUtils.getField(sourceField, source);
                ReflectionUtils.setField(targetField, target, value);
            }
            return target;
        } catch (Exception e) {
            log.error("copy source properties to target obj error", e);
            return null;
//            throw new GenericBizException(HttpStatus.BAD_REQUEST, "Copy properties failure, " + e.getMessage());
        }
    }

    public static Map<String, Object> convertObjToMap(Object source, String... ignoreProperties) {
        Assert.notNull(source, "Source must not be null");

        var targetDto = new HashMap<String, Object>();
        try {
            var sourceFields = getClassAllDeclaredFields(source.getClass());
            for (Field sourceField : sourceFields) {
                ReflectionUtils.makeAccessible(sourceField);
                var key = sourceField.getName();
                if (!StringUtils.hasText(key) || Arrays.asList(ignoreProperties).contains(key)) {
                    continue;
                }
                targetDto.put(key, ReflectionUtils.getField(sourceField, source));
            }
            return targetDto;
        } catch (Exception e) {
            log.error("convert obj to map error", e);
            return null;
//            throw new GenericBizException(HttpStatus.BAD_REQUEST, "Copy properties failure, " + e.getMessage());
        }
    }

    public static List<Field> getClassAllDeclaredFields(final Class<?> clazz) {
        var fields = new ArrayList<Field>();
        var tmpClazz = clazz;
        while (tmpClazz != null) {
            fields.addAll(Arrays.asList(tmpClazz.getDeclaredFields()));
            tmpClazz = tmpClazz.getSuperclass();
        }
        return fields.stream().filter(field -> !Modifier.isStatic(field.getModifiers())).collect(Collectors.toList());
    }

    public static List<Field> getClassDeclaredFields(final Class<?> clazz) {
        var fields = clazz.getDeclaredFields();
        return Arrays.stream(fields).filter(field -> !Modifier.isStatic(field.getModifiers()))
            .collect(Collectors.toList());
    }

    public static String convertToString(@Nullable Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean) {
            return String.valueOf(((Boolean) value).booleanValue());
        }
        if (value instanceof Integer) {
            return String.valueOf(((Integer) value).intValue());
        }
        if (value instanceof Instant) {
            return String.valueOf(((Instant) value).toEpochMilli());
        }
        if (value instanceof Long) {
            return String.valueOf(((Long) value).longValue());
        }
        return value.toString();
    }

    @Nullable
    public static Object getFieldValue(Object value, Field field) {
        try {
            ReflectionUtils.makeAccessible(field);
            return field.get(value);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static Set<String> filterClassDefinedFields(Set<String> fields, Class<?> clazz) {
        if (fields == null) {
            return Set.of();
        }
        var definedFields = getClassAllDeclaredFields(clazz).stream().map(Field::getName).collect(toList());
        return fields.stream().filter(definedFields::contains).collect(toSet());
    }

    public static Set<String> filterClassDefinedFields(Set<String> fields, Class<?> clazz, Set<String> excludeFields) {
        return filterClassDefinedFields(fields, clazz).stream().filter(field -> !excludeFields.contains(field))
            .collect(toSet());
    }

    public static String truncateFieldValue(String fieldValue, int maxLength) {
        if (StringUtils.hasText(fieldValue) || fieldValue.length() <= maxLength) {
            return fieldValue;
        }
        return fieldValue.substring(0, maxLength);
    }

    public static boolean isSubclassOf(Type subclass, Class<?> superclass) {
        if (!(subclass instanceof Class)) {
            return false;
        }
        return superclass.isAssignableFrom((Class<?>) subclass);
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getGenericTypeClass(Class<?> implClass, Class<T> targetClass) {
        var type = implClass.getGenericSuperclass();
        var exception = new IllegalStateException("Unable to determine the generic type");
        if (!(type instanceof ParameterizedType)) {
            throw exception;
        }
        var parameterizedType = (ParameterizedType) type;
        var typeArguments = parameterizedType.getActualTypeArguments();
        if (typeArguments.length == 0) {
            throw exception;
        }
        for (Type typeArgument : typeArguments) {
            if (isSubclassOf(typeArgument, targetClass)) {
                return (Class<T>) typeArgument;
            }
        }
        throw exception;
    }

    public static boolean isJavaClass(Class<?> clz) {
        return clz != null && clz.getClassLoader() == null;
    }
}
