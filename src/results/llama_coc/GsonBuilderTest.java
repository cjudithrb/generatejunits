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
import org.junit.Before; 
import org.junit.Test;
import java.lang.reflect.Modifier;
import static org.junit.Assert.*;
import java.util.Date;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonDeserializer; 
import com.google.gson.JsonDeserializationContext; 
import com.google.gson.JsonParseException; 
import java.lang.reflect.Type; 


import org.junit.Test;
import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject; 
import com.google.gson.JsonParser;

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

class User {
    private String name;

    public User(String name) {
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

    @Test
    public void testSetVersion() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setVersion(2);
        Gson gson = gsonBuilder.create();
        // test
    }

    @Test
    public void generateNonExecutableJson() throws Exception {
      Gson gson = new GsonBuilder().generateNonExecutableJson().create();
      JsonElement element = gson.toJsonTree("{ \"a\" : \"b\" }");
      assertEquals("/* non-executable JSON */{ \"a\" : \"b\" }", element.toString());
    }
 
    /*@Test
    public void testExcludeFieldsWithoutExposeAnnotation() {
        // Arrange
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        // Act
        String json = gson.toJson(new User());

        // Assert
        assertEquals("{\"name\":\"TestUser\"}", json);
    }

    @Test
    public void testExcludeFieldsWithoutExposeAnnotation2() {
        // Arrange
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        // Act
        String json = gson.toJson(new User2());

        // Assert
        assertTrue(json.contains("\"age\":null"));
    }

    @Test
    public void testExcludeFieldsWithoutExposeAnnotation3() {
        // Arrange
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        // Act
        String json = gson.toJson(new User3());

        // Assert
        assertTrue(json.contains("\"age\":null"));
    }*/

  @Test
  public void testSerializeNulls() {
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.serializeNulls().create();
    Gson gson2 = builder.create();
    String json = gson.toJson(new Person("John", 25));
    System.out.println(json);
    json = gson2.toJson(new Person("John", 25));
    System.out.println(json);
  }

    @Test
    public void testDisableInnerClassSerialization() throws Exception {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.disableInnerClassSerialization();
        Gson gson = gsonBuilder.create();
        // Verify that inner classes are not serialized
        String json = gson.toJson(new A());
        assertEquals("{}", json);
    }

    private static class A {
        private B b = new B();
    }

    private static class B {
    }

    /*@Test
    public void setLongSerializationPolicyTest() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setLongSerializationPolicy(DeserializationPolicy.DEFAULT).create();
        // TODO: implement this test
    }  */

    private GsonBuilder gsonBuilder = new GsonBuilder();

    @Test
    public void testSetFieldNamingPolicy() {
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        Gson gson = gsonBuilder.create();

        assertEquals("upperCamelCase", gson.toJson(Person.class).toString());
    }

    /*@Test
    public void testFieldNamingStrategy() {
        GsonBuilder builder = new GsonBuilder();
        FieldNamingStrategy fieldNamingStrategy = new LowerCaseFieldNamingStrategy();
        builder.setFieldNamingStrategy(fieldNamingStrategy);
        Gson gson = builder.create();
        String json = gson.toJson(new User("John Doe"));
        System.out.println(json);
        assertEquals("{\"name\":\"john doe\"}", json);
    }*/
    
    @Test
    public void test() throws Exception {
        //write your test here
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getName().equals("toString");
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return clazz.getName().equals("String");
            }
        }).create();
        String json = gson.toJson(list);
        
    }        

    /*@Test
    public void testDisableHtmlEscaping() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = gson.toJson(new Person());
        assertThat(json, hasProperty("bar"));
    }

    @Test
    public void testDisableInnerClassEscaping() {
        Gson gson = new GsonBuilder().disableInnerClassEscaping().create();
        String json = gson.toJson(new Person());
        assertThat(json, hasProperty("bar"));
    }*/

  @Test
  public void testDateFormat() {
    GsonBuilder builder = new GsonBuilder();
    builder.setDateFormat("yyyy-MM-dd");
    Gson gson = builder.create();
    Date date = new Date();
    String json = gson.toJson(date);
    assertEquals("2016-10-11", json);
  }

    @Before
    public void setUp() {
        gsonBuilder = new GsonBuilder();
    }

   /* @Test
    public void setDateFormat() {
        // Arrange
        String dateStyle = "SHORT";
        String timeStyle = "MEDIUM";

        // Act
        gsonBuilder.setDateFormat(dateStyle, timeStyle);

        // Assert
        assertEquals(dateStyle, gsonBuilder.getDateFormat().getStyle());
        assertEquals(timeStyle, gsonBuilder.getDateFormat().getTimeStyle());
    }

    @Test
    public void testRegisterTypeAdapter() throws Exception {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Person.class, new MyClassAdapter());
        Gson gson = gsonBuilder.create();
        // test serialization and deserialization
        Person instance = gson.fromJson("{\"id\": 1, \"name\": \"foo\"}", Person.class);
        //assertEquals(1, instance.getId());
        assertEquals("foo", instance.getName());
    }*/

    @Test
    public void testRegisterTypeHierarchyAdapter() throws Exception {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeHierarchyAdapter(String.class, new JsonSerializer<String>() {
            @Override
            public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src);
            }
        });
        Gson gson = builder.create();
        assertEquals("1", gson.toJson(new String("1")));
        assertEquals("2", gson.toJson(new String("2")));
    }

    @Test
    public void testSerializeSpecialFloatingPointValues() throws Exception {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.serializeSpecialFloatingPointValues();
    Gson gson = gsonBuilder.create();
    String json = gson.toJson(Double.NaN);
    assertEquals("[\"NaN\"]", json);
    json = gson.toJson(Double.POSITIVE_INFINITY);
    assertEquals("[\"Infinity\"]", json);
    json = gson.toJson(Double.NEGATIVE_INFINITY);
    assertEquals("[\"-Infinity\"]", json);
    json = gson.toJson(Double.MAX_VALUE);
    assertEquals("[\"Infinity\"]", json);
    json = gson.toJson(Double.MIN_VALUE);
    assertEquals("[\"-Infinity\"]", json);
    json = gson.toJson(Double.MAX_VALUE);
    assertEquals("[\"Infinity\"]", json);
    json = gson.toJson(Double.MAX_VALUE);
    assertEquals("[\"Infinity\"]", json);
    json = gson.toJson(Double.MIN_VALUE);
    assertEquals("[\"-Infinity\"]", json);
    json = gson.toJson(Double.NaN);
    assertEquals("[\"NaN\"]", json);
    }
    
}