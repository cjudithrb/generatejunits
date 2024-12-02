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

class Person {
  private String name;
  private int age;
  public Person() {
  }
  public Person(String name, int age) {
    this.name = name;
    this.age = age;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public int getAge() {
    return age;
  }
  public void setAge(int age) {
    this.age = age;
}}


public class GsonBuilderTest {

    private GsonBuilder gsonBuilder;

   /* @BeforeEach
    public void setUp() {
        gsonBuilder = new GsonBuilder();
    }*/

    @Test
    public void shouldExcludeFieldsWithoutExposeAnnotation() {
        // Option 1: Verify the return value expected
        // Define the expected result for different input of the method
        // Ensure to include both typical and edge cases

        // Option 2: Evaluate the behavior of the method against different inputs
        // Consider possible valid and invalid inputs that the method could receive
        // Ensure to verify that the method handles errors or unexpected values correctly

        // Option 3: Evaluate the side effects of the method
        // If the method modifies some state or global variable, verify that the changes are expected
        // Ensure that there are no unintended side effects

        // Assertions
        // Verify the return value expected
        // Verify the behavior of the method against different inputs
        // Verify the side effects of the method
    }

    @Test
    void testSerializeNulls() {
        // Define the expected result for different inputs of the method
        // A) Valid input
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.serializeNulls().create();
        String json = gson.toJson(new Person("John", 25));
        assert json!= null;
        // B) Invalid input
        json = gson.toJson(new Person(null, 25));
        assert json == null;
        // C) Edge case
        json = gson.toJson(new Person("John", 25));
        assert json!= null;
    }
    
    @Test
    void testSerializeNulls2() {
        // Evaluate the behavior of the method under different inputs
        // A) Valid input
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.serializeNulls().create();
        String json = gson.toJson(new Person("John", 25));
        assert json!= null;
        // B) Invalid input
        json = gson.toJson(new Person(null, 25));
        assert json == null;
        // C) Edge case
        json = gson.toJson(new Person("John", 25));
    }

    @Test
    void testSetFieldNamingPolicy() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        // TODO: implementar test case aquí
    }

    @Test
    public void testSetPrettyPrinting() {
        // TODO: implementar test aquí
    }

    @Test
    public void testSetPrettyPrintingWithInvalidInput() {
        // TODO: implementar test aquí
    }

    @Test
    public void testSetPrettyPrintingWithInvalidInput2() {
        // TODO: implementar test aquí
    }
    
    @Test
    void testSetDateFormat() {
        // Arrange
        GsonBuilder builder = new GsonBuilder();
        String pattern = "dd-MM-yyyy";

        // Act
        builder.setDateFormat(pattern);

        // Assert
        //assertEquals(pattern, builder.getDateFormat());
    }

    @Test
    void testSetDateFormatInvalidPattern() {
        // Arrange
        GsonBuilder builder = new GsonBuilder();
        String pattern = "invalid pattern";

        // Act
        builder.setDateFormat(pattern);

        // Assert
        //assertEquals("", builder.getDateFormat());
    }

    @Test
    void testSetDateFormatWithGlobalState() {
        // Arrange
        GsonBuilder builder = new GsonBuilder();
        String pattern = "dd-MM-yyyy";

        // Act
        builder.setDateFormat(pattern);
       // builder.setGlobalState(true);

        // Assert
        //assertTrue(builder.getGlobalState());
    }

    @Test
    void testSerializeSpecialFloatingPointValues() {
        // OPTION 1: Verify the expected result
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeSpecialFloatingPointValues();
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(Double.NaN);
        assertEquals("[\"NaN\"]", json);
    }
    
    /*@BeforeEach
    public void setup3() {
        gsonBuilder = new GsonBuilder();
    }*/

    @Test
    public void createTest() {
        // Option 1: Verify expected return value
        Gson gson = gsonBuilder.create();
        assertEquals("Expected return value", "Expected return value", gson);

        // Option 2: Evaluate behavior of method
        Gson gson2 = gsonBuilder.create();
        assertEquals("Expected return value", "Expected return value", gson2);

        // Option 3: Evaluate side effects
        Gson gson3 = gsonBuilder.create();
        assertEquals("Expected return value", "Expected return value", gson3);
    }    
}