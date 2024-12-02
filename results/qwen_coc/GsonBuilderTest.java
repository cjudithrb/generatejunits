/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DateFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.lang.reflect.Modifier;
import com.google.gson.ExclusionStrategy; 
import com.google.gson.FieldAttributes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.gson.Gson; 
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.JsonPrimitive; 
import java.math.BigDecimal; // 
import java.lang.reflect.Field; // Importa Field 
import java.util.List; // Importa List
import java.util.ArrayList; // Importa ArrayList
import com.google.gson.stream.JsonReader; // Importa JsonReader 
import com.google.gson.stream.JsonWriter; // Importa JsonWriter 
import java.io.IOException; // Importa IOException 
import java.io.StringWriter; // Importa StringWriter si también la estás usando 
import java.io.StringReader;

class MyClass {
    private String name;

    public MyClass(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

public class GsonBuilderTest {

    /*@Test
    public void testSetVersion() {
        GsonBuilder builder = new GsonBuilder();
        builder.setVersion(1.0);
        assertEquals(1.0, builder.getVersion(), 0.0);
    }*/

    @Test
    public void testExcludeFieldsWithModifiers() {
        // Arrange
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC);

        // Act
        Gson gson = builder.create();

        // Assert
        //assertTrue(gson.getExclusionStrategy().isExcluded("transientField"));
        //assertTrue(gson.getExclusionStrategy().isExcluded("staticField"));
        //assertFalse(gson.getExclusionStrategy().isExcluded("nonTransientField"));
        //assertFalse(gson.getExclusionStrategy().isExcluded("nonStaticField"));
    }
    /*
    @Test
    public void testExcludeFieldsWithoutExposeAnnotation() {
        // Arrange
        GsonBuilder builder = new GsonBuilder();

        // Act
        builder.excludeFieldsWithoutExposeAnnotation();

        // Assert
        assertTrue(builder.excludeFieldsWithoutExposeAnnotation());
    }*/

    @Test
    public void testSerializeNulls() {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.create();
        // Add your test cases here
    }

    /*@Test
    public void testDisableInnerClassSerialization() {
        GsonBuilder builder = new GsonBuilder();
        builder.disableInnerClassSerialization();
        assertTrue(builder.disableInnerClassSerialization());
    }

    @Test
    public void testSetLongSerializationPolicy() {
        // Arrange
        GsonBuilder builder = new GsonBuilder();

        // Act
        builder.setLongSerializationPolicy(LongSerializationPolicy.SOME_POLICY);

        // Assert
        //assertEquals(LongSerializationPolicy.SOME_POLICY, builder.getLongSerializationPolicy());
    }*/

    @Test
    public void testSetFieldNamingPolicy() {
        // Arrange
        GsonBuilder builder = new GsonBuilder();
        String expectedFieldName = "expectedFieldName";
        String actualFieldName = "actualFieldName";

        // Act
        builder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        builder.create().toJson(new Object());

        // Assert
        assertEquals(expectedFieldName, actualFieldName);
    }

   /* @Test
    public void testSetFieldNamingStrategy() {
        // Arrange
        GsonBuilder builder = new GsonBuilder();
        FieldNamingStrategy strategy = new FieldNamingStrategy() {
            @Override
            public String translateName(String name) {
                return name.toUpperCase();
            }
        };

        // Act
        builder.setFieldNamingStrategy(strategy);

        // Assert
        assertEquals(strategy, builder.getFieldNamingStrategy());
    }
    
    @Test
    public void testSetExclusionStrategies() {
        // Arrange
        GsonBuilder builder = new GsonBuilder();
        List<ExclusionStrategy> strategies = Arrays.asList(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getName().equals("password");
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return clazz.getName().equals("com.example.User");
            }
        });

        // Act
        builder.setExclusionStrategies(strategies);

        // Assert
        //assertTrue(builder.getExclusionStrategies().containsAll(strategies));
    }*/

    @Test
    public void testSetPrettyPrinting() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
       // assertTrue(builder.getPrettyPrinting());
    }

    @Test
    public void testDisableHtmlEscaping() {
        GsonBuilder builder = new GsonBuilder();
        builder.disableHtmlEscaping();
       // assertTrue(builder.isEnabled(GsonBuilder.Feature.DISABLE_HTML_ESCAPE));
    }

    /*@Test
    public void testSetDateFormat() {
        // Arrange
        GsonBuilder builder = new GsonBuilder();
        SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Act
        builder.setDateFormat(pattern);

        // Assert
        assertNotNull(builder.getDateFormat());
        assertEquals(pattern, builder.getDateFormat());
    }

    @Test
    public void testSetDateFormatMultipleTimes2() {
        // Arrange
        GsonBuilder builder = new GsonBuilder();
        SimpleDateFormat pattern1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat pattern2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Act
        builder.setDateFormat(pattern1);
        builder.setDateFormat(pattern2);

        // Assert
        assertNotNull(builder.getDateFormat());
        assertEquals(pattern2, builder.getDateFormat());
    }

    @Test
    public void testSetDateFormatNull() {
        // Arrange
        GsonBuilder builder = new GsonBuilder();

        // Act
        builder.setDateFormat(null);

        // Assert
        assertNull(builder.getDateFormat());
    }

    @Test
    public void testSetDateFormat2() {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd");
        assertEquals("yyyy-MM-dd", builder.getDateFormat());
    }

    @Test
    public void testSetDateFormat3() {
        // Arrange
        GsonBuilder builder = new GsonBuilder();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        builder.setDateFormat(dateFormat);

        // Act
        Date date = new Date();
        String serializedDate = builder.create().toJson(date);

        // Assert
        assertNotNull(serializedDate);
        assertEquals(dateFormat.format(date), serializedDate);
    }

    @Test
    public void testSetDateFormatMultipleTimes() {
        // Arrange
        GsonBuilder builder = new GsonBuilder();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        builder.setDateFormat(dateFormat);
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        builder.setDateFormat(dateFormat2);

        // Act
        Date date = new Date();
        String serializedDate = builder.create().toJson(date);

        // Assert
        assertNotNull(serializedDate);}

    @Test
    public void testRegisterTypeAdapter() {
        GsonBuilder builder = new GsonBuilder();
        TypeAdapter<String> typeAdapter = new TypeAdapter<String>() {
            @Override
            public String deserialize(JsonReader in) throws IOException {
                return in.nextString();
            }

            @Override
            public void serialize(String value, JsonWriter out) throws IOException {
                out.value(value);
            }
        };

        builder.registerTypeAdapter(String.class, typeAdapter);

        Gson gson = builder.create();
        String json = gson.toJson("Hello, World!");
        String deserialized = gson.fromJson(json, String.class);

        assertEquals("Hello, World!", deserialized);
    }

    @Test
    public void testRegisterTypeHierarchyAdapter() {
        // Arrange
        GsonBuilder builder = new GsonBuilder();
        Class<?> baseType = String.class;
        TypeAdapter<?> typeAdapter = new TypeAdapter<String>() {
            // Implement the required interfaces
        };

        // Act
        builder.registerTypeHierarchyAdapter(baseType, typeAdapter);

        // Assert
        // Verificar que el adaptador de tipo haya sido registrado correctamente
    }

    @Test
    public void testSerializeSpecialFloatingPointValues() {
        // Arrange
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Act
        gsonBuilder.serializeSpecialFloatingPointValues();

        // Assert
        assertTrue(gsonBuilder.serializeSpecialFloatingPointValues());
    }
    
    @Test
    public void testCreate() {
        // Arrange
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(MyClass.class, new MyClass());
        builder.serializeNulls();
        builder.setPrettyPrinting();

        // Act
        Gson gson = builder.create();

        // Assert
        assertNotNull(gson);
        assertEquals(MyClass.class, gson.getAdapter(MyClass.class).getType());
        assertTrue(gson.getSerializationConfig().isSerializeNulls());
        assertTrue(gson.getSerializationConfig().isPrettyPrinting());
    }*/
}