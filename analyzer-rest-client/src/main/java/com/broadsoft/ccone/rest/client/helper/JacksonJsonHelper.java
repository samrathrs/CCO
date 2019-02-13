/**
 *
 */
package com.broadsoft.ccone.rest.client.helper;

import java.io.IOException;
import java.util.Map;

import com.broadsoft.ccone.rest.client.exception.JsonException;
import com.broadsoft.ccone.rest.client.exception.XmlException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author svytla
 */
public class JacksonJsonHelper {

    public static String serialize(final Object object) throws JsonException {

        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            throw new JsonException("Error in serializing object", e);
        }

    }

    public static String serialize(final Object object, final Class<?> clazz) throws JsonException {

        final ObjectWriter writer = new ObjectMapper().writerFor(clazz);
        try {
            return writer.writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            throw new JsonException("Error in serializing object", e);
        }

    }

    public static String serialize(final Object object, final TypeReference<?> typeRef) throws JsonException {
        final ObjectWriter writer = new ObjectMapper().writerFor(typeRef);
        try {
            return writer.writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            throw new JsonException("Error in serializing object", e);
        }

    }

    public static String serializeXml(final Object object, final Class<?> clazz) throws XmlException {
        final ObjectWriter writer = new XmlMapper().writerFor(clazz);
        try {
            return writer.writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            throw new XmlException("Error in serializing object", e);
        }
    }

    public static <T> T deserialize(final String json, final Class<T> clazz) throws JsonException {

        final ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(json, clazz);
        } catch (final IOException e) {
            throw new JsonException(String.format("Error in deserializing json %s", json), e);
        }

    }

    public static <T> T deserialize(final String json, final TypeReference<?> typeRef) throws JsonException {

        final ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(json, typeRef);
        } catch (final IOException e) {
            throw new JsonException(String.format("Error in deserializing json %s", json), e);
        }
    }

    public static <T> T deserializeXml(final String xml, final Class<T> clazz) throws JsonException, XmlException {
        final XmlMapper xmlMapper = new XmlMapper();
        try {
            return xmlMapper.readValue(xml, clazz);
        } catch (final IOException e) {
            throw new XmlException(String.format("Error in deserializing xml %s", xml), e);
        }
    }

    public static <T> T convertValue(final Map<String, Object> map, final Class<T> clazz) throws JsonException {
        final ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.convertValue(map, clazz);
        } catch (final Exception e) {
            throw new JsonException(String.format("Error in converting map %s to pojo of %s", map, clazz), e);
        }
    }

}
