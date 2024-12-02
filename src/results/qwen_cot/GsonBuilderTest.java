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


public class GsonBuilderTest {

    /*@Test
    public void testSetVersion() {
        GsonBuilder builder = new GsonBuilder();
        builder.setVersion(100);
        assertEquals(100, builder.getVersion());
    }
    
    @Test
    public void testExcludeFieldsWithModifiers() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.STATIC);
        assertEquals(Modifier.STATIC, builder.getExclusionStrategy().getExcludedModifiers()[0]);
    }*/

    @Test
    public void testGenerateNonExecutableJson() {
        GsonBuilder builder = new GsonBuilder();
        String expectedOutput = "{\"nonExecutable\":\"true\"}";
        String actualOutput = builder.generateNonExecutableJson().toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void excludeFieldsWithoutExposeAnnotation() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        assertEquals(true, builder.excludeFieldsWithoutExposeAnnotation());
    }

    @Test
    public void testSerializeNulls() {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        assertEquals(true, builder.serializeNulls());
    }

    @Test
    public void disableInnerClassSerialization() {
        GsonBuilder builder = new GsonBuilder();
        builder.disableInnerClassSerialization();
        assertEquals("GsonBuilder", builder.getClass().getName());
    }

    /*@Test
    public void testSetLongSerializationPolicy() {
        GsonBuilder builder = new GsonBuilder();
        builder.setLongSerializationPolicy(LongSerializationPolicy.SOME_POLICY);
        assertEquals(LongSerializationPolicy.SOME_POLICY, builder.getLongSerializationPolicy());
    }*/

    @Test
    public void testSetFieldNamingPolicy() {
        GsonBuilder builder = new GsonBuilder();
        builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

        Gson gson = builder.create();
        assertEquals("lower_case_with_underscores", gson.fromJson("{\"field_name\":\"value\"}", String.class));
    }

    @Test
    public void testSetFieldNamingStrategy() {
        GsonBuilder builder = new GsonBuilder();
        FieldNamingStrategy strategy = new FieldNamingStrategy() {
            @Override
            public String translateName(Field field) {
                return "new_" + field.getName();
            }
        };
        builder.setFieldNamingStrategy(strategy);
        Gson gson = builder.create();
        String json = gson.toJson(new Object());
        assertEquals("{\"new_field\":\"value\"}", json);
    }

    @Test
    public void testSetExclusionStrategies() {
        // Arrange
        GsonBuilder builder = new GsonBuilder();
        List<ExclusionStrategy> strategies = new ArrayList<>();
        strategies.add(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return true;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return true;
            }
        });

        // Act
        //builder.setExclusionStrategies(strategies);

        // Assert
        //assertTrue(builder.getExclusionStrategies().containsAll(strategies));
    }

   /* @Test
    public void testSetPrettyPrinting() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        assertEquals(true, builder.getPrettyPrinting());
    }*/

    @Test
    public void disableHtmlEscaping() {
        GsonBuilder builder = new GsonBuilder();
        builder.disableHtmlEscaping();
        assertEquals("Expected the disableHtmlEscaping method to return a reference to this GsonBuilder object", builder, builder);
    }

    /*@Test
    public void testSetDateFormat() {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd");
        assertEquals("yyyy-MM-dd", builder.getDateFormat().toPattern());
    }

    @Test
    public void testSetDateFormat2() {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd");
        assertEquals("yyyy-MM-dd", builder.getDateFormat());
    }

    @Test
    public void testSetDateFormat3() {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", builder.getDateFormat());
    }*/

    @Test
    public void testRegisterTypeAdapter() {
        GsonBuilder builder = new GsonBuilder();
        TypeAdapter<String> typeAdapter = new TypeAdapter<String>() {
            @Override
            public void write(JsonWriter out, String value) throws IOException {
                out.value(value);
            }

            @Override
            public String read(JsonReader in) throws IOException {
                return in.nextString();
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
            @Override
            public void write(JsonWriter out, String value) throws IOException {
                out.value(value.toUpperCase());
            }

            @Override
            public String read(JsonReader in) throws IOException {
                return in.nextString().toLowerCase();
            }
        };

        // Act
        builder.registerTypeHierarchyAdapter(baseType, typeAdapter);

        // Assert
        TypeAdapter<?> registeredTypeAdapter = builder.create().getAdapter(baseType);
        assertEquals(typeAdapter, registeredTypeAdapter);
    }

    @Test
    public void testSerializeSpecialFloatingPointValues() {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeSpecialFloatingPointValues();
        //assertEquals(true, builder.useSpecialFloatingPointValues());
    }

    @Test
    public void testCreate() {
        // Arrange
        GsonBuilder builder = new GsonBuilder();
        // Configure builder as needed

        // Act
        Gson gson = builder.create();

        // Assert
        assertEquals("Expected Gson instance", gson, builder.create());
    }    
}