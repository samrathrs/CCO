/**
 *
 */
package com.broadsoft.ccone.rest.client.helper;

import java.io.StringReader;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;

import com.broadsoft.ccone.rest.client.exception.JsonException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

/**
 * @author svytla
 */
public class JsonHelper {

    /**
     * @param json
     * @param calzz
     * @return
     */
    public static <T> T deserialize(final String json, final Class<T> calzz) throws JsonException {

        final Gson gson = new Gson();

        try {
            final JsonReader reader = new JsonReader(new StringReader(json));
            reader.setLenient(true);
            final T object = gson.fromJson(reader, calzz);
            return object;
        } catch (final JsonSyntaxException e) {
            throw new JsonException(String.format("Error in deserializing json %s", json), e);
        }

    }

    public static <T> T deserialize(final String json, final Type typeOfT) throws JsonException {

        final Gson gson = new Gson();

        try {
            final JsonReader reader = new JsonReader(new StringReader(json));
            reader.setLenient(true);
            final T object = gson.fromJson(reader, typeOfT);
            return object;
        } catch (final JsonSyntaxException e) {
            throw new JsonException(String.format("Error in deserializing json %s", json), e);
        }
    }

    /**
     * @param object
     * @param calzz
     * @return
     */
    public static String serialize(final Object object, final Class<?> calzz) {

        final Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create();
        final String json = gson.toJson(object, calzz);
        return json;

    }

    public static String serialize(final Object object, final Type typeOfT) {
        final Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create();
        final String json = gson.toJson(object, typeOfT);
        return json;
    }

    /**
     * @param object
     * @return
     */
    public static String serialize(final Object object) throws JsonException {

        final Gson gson = new Gson();
        try {
            final String json = gson.toJson(object);
            return json;
        } catch (final JsonIOException e) {
            throw new JsonException("Error in serilizing object to json", e);
        }

    }

    /**
     * @param object
     * @return
     */
    public static String serializePrettyPrint(final Object object) throws JsonException {

        final Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithModifiers(Modifier.TRANSIENT).create();
        try {
            final String json = gson.toJson(object);
            return json;
        } catch (final JsonIOException e) {
            throw new JsonException("Error in serilizing object to json", e);
        }
    }

    /**
     * @param object
     * @return
     */
    public static String serializeWithLineFeed(final List<?> objects) throws JsonException {

        final StringBuilder builder = new StringBuilder();

        boolean first = true;

        if (objects != null) {
            for (final Object obj : objects) {

                if (!first) {
                    builder.append("\n");
                }

                first = false;
                builder.append(serialize(obj));
            }
        }

        return builder.toString();
    }

}
