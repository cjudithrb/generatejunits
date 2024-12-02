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

import com.google.gson.JsonSerializationContext; 
import com.google.gson.JsonSerializer; 
import java.lang.reflect.Type;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.assertEquals;

public class GsonBuilderTest {

    @Test
    public void testSetVersionReturnsGsonBuilder() {
        GsonBuilder builder = new GsonBuilder();
        GsonBuilder result = builder.setVersion(1.0);
        assertEquals(builder, result);
    }
    
    @Test
    public void testExcludeFieldsWithModifiers() {
        // Define the expected return value for different input values
        GsonBuilder builder = new GsonBuilder();
        assertEquals(builder, builder.excludeFieldsWithModifiers(Modifier.TRANSIENT));
        assertEquals(builder, builder.excludeFieldsWithModifiers(Modifier.STATIC));
        //assertEquals(builder, builder
    }
    
    @Test
    public void testExcludeFieldsWithoutExposeAnnotation() {
        // Opción 1: Verificar el valor de retorno esperado del método
        GsonBuilder builder = new GsonBuilder();
        assertNull(builder.excludeFieldsWithoutExposeAnnotation());

        // Opción 2: Evaluar el comportamiento del método ante diferentes entradas
        builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        assertEquals(builder.excludeFieldsWithoutExposeAnnotation(), true);

        // Opción 3: Evaluar los efectos secundarios del método
        builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        assertNull(builder.excludeFieldsWithoutExposeAnnotation());
    }

    @Test
    public void testSerializeNulls() {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        assertEquals(true, builder.serializeNulls());
    }

    @Test
    public void testSerializeNulls2() {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        assertEquals(true, builder.serializeNulls());
    }

    @Test
    public void testDisableInnerClassSerialization() {
        GsonBuilder builder = new GsonBuilder();
        builder.disableInnerClassSerialization();
        assertEquals(true, builder.disableInnerClassSerialization());
    }

    /*@Test
    public void testSetLongSerializationPolicy() {
        GsonBuilder builder = new GsonBuilder();
        builder.setLongSerializationPolicy(LongSerializationPolicy.DEFAULT);
        assertEquals(LongSerializationPolicy.DEFAULT, builder.getLongSerializationPolicy());
    }

    @Test
    public void testSetFieldNamingPolicy_returnsCorrectReference() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        assertEquals(builder, gson.getGsonBuilder());
    }

    @Test
    public void testSetFieldNamingStrategy_returnsCorrectReference() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setFieldNamingStrategy(new CaseInsensitiveStrategy()).create();
        assertEquals(builder, gson.getGsonBuilder());
    }

    @Test
    public void testSetPrettyPrinting_returnsGsonBuilder() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        assertEquals(builder, gson.getJsonSerializer());
    }

    @Test
    public void testSetPrettyPrinting_withValidInput() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        assertEquals(builder, gson.getJsonSerializer());
    }*/

    @Test
    public void testDisableHtmlEscaping() {
        GsonBuilder builder = new GsonBuilder();
        builder.disableHtmlEscaping();
        assertEquals(true, builder.disableHtmlEscaping());
    }

    @Test
    public void testSerializeSpecialFloatingPointValues() {
        // Opción 1: Verificar el valor de retorno esperado del método
        GsonBuilder builder = new GsonBuilder();
        builder.serializeSpecialFloatingPointValues();
        assertEquals(GsonBuilder.class, builder.getClass());

        // Opción 2: Evaluar el comportamiento del método ante diferentes entradas
        try {
            builder.serializeSpecialFloatingPointValues();
            assertThrows(IllegalArgumentException.class, () -> builder.serializeSpecialFloatingPointValues());
        } catch (Exception e) {
            // Manejar excepciones si es necesario
        }

        // Opción 3: Evaluar los efectos secundarios del método
        // No hay efectos secundarios en este caso, pero se puede agregar una prueba para verificar si el método modifica algún estado o variable global
        // Por ejemplo:
        // Gson gson = new Gson();
        // String json = gson.toJson(1.0f);
        // assertEquals("1.0", json);
    }
    
    @Test
    public void testCreate() {
        // Arrange
        GsonBuilder builder = new GsonBuilder();
        // Set up the builder with desired options

        // Act
        Gson gson = builder.create();

        // Assert
        assertEquals("Expected Gson instance", gson, builder.create());
    }
}