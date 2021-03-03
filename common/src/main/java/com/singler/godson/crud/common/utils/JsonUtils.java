package com.singler.godson.crud.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

/**
 * JSON 工具
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/11/12 16:56
 */
public class JsonUtils {
    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    public static String toJson(Object o) {
        StringWriter sw = new StringWriter();
        JsonGenerator gen = null;
        try {
            gen = (new JsonFactory()).createGenerator(sw);
            OBJECT_MAPPER.writeValue(gen, o);
        } catch (IOException var11) {
            throw new RuntimeException("不能序列化对象为Json", var11);
        } finally {
            if (null != gen) {
                try {
                    gen.close();
                } catch (IOException var10) {
                    throw new RuntimeException("不能序列化对象为Json", var10);
                }
            }

        }

        return sw.toString();
    }

    public static Map<String, Object> toMap(Object o) {
        return (Map) OBJECT_MAPPER.convertValue(o, Map.class);
    }

    public static <T> T toBean(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException var3) {
            throw new RuntimeException("将 Json 转换为对象时异常,数据是:" + json, var3);
        }
    }

    public static <T> List<T> toList(String json, Class<T> clazz) throws IOException {
        JavaType type = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
        return OBJECT_MAPPER.readValue(json, type);
    }

    public static <T> T[] toArray(String json, Class<T[]> clazz) throws IOException {
        return OBJECT_MAPPER.readValue(json, clazz);
    }

    public static <T> T toObject(JsonNode jsonNode, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.treeToValue(jsonNode, clazz);
        } catch (JsonProcessingException var3) {
            throw new RuntimeException("将 Json 转换为对象时异常,数据是:" + jsonNode.toString(), var3);
        }
    }

    public static JsonNode toNode(Object o) {
        try {
            return o == null ? OBJECT_MAPPER.createObjectNode() : OBJECT_MAPPER.convertValue(o, JsonNode.class);
        } catch (Exception var2) {
            throw new RuntimeException("不能序列化对象为Json", var2);
        }
    }
}
