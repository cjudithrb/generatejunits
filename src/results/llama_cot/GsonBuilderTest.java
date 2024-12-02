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

class Person {
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

class TestExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getName().equals("age");
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}

public class GsonBuilderTest {

   /* public void testSetVersion() {
      Gson gson = new GsonBuilder().setVersion(1.0).create();
      assertEquals(1.0, gson.getSerializationVersion());
    }
    
    public void testSetVersion2() {
      Gson gson = new GsonBuilder().setVersion(1.0).create();
      assertEquals(1.0, gson.getSerializationVersion());
    }*/

    private Gson gson;
    private GsonBuilder builder;
    
    @Before
    public void setUp() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithModifiers(Modifier.STATIC);
        gson = gsonBuilder.create();
    }

    @Test
    public void shouldExcludeStaticFields() {
        Person person = new Person();
        person.setAge(30);

        String json = gson.toJson(person);

        assertEquals("{\"age\":30}", json);
    }
    
    @Before
    public void setUp2() throws Exception {
      this.gson = new GsonBuilder().create();
    }

    @Test
    public void testGenerateNonExecutableJson() {
      // TODO: implement
    }

    /*@Test
    public void shouldExcludeFieldsWithoutExposeAnnotation() {
        Gson gson = builder.excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(new Person("John", "Doe"));
        Person user = gson.fromJson(json, Person.class);
        assertEquals("John", user.getName());
        assertEquals("Doe", user.getSurname());
    }    */

    public void testSerializeNulls() {
      GsonBuilder gsonBuilder = new GsonBuilder();
      gsonBuilder.serializeNulls();
      Gson gson = gsonBuilder.create();
      String json = gson.toJson(null);
      assertEquals("{\"foo\":null}", json);
    }    

    @Test
    public void testSerializeNulls2() {
      GsonBuilder gsonBuilder = new GsonBuilder();
      gsonBuilder.serializeNulls();
      Gson gson = gsonBuilder.create();
      String json = gson.toJson(null);
      assertEquals("{\"foo\":null}", json);
    }

    public void testDisableInnerClassSerialization() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.disableInnerClassSerialization().create();
        //...
    }

    /*@Test
    public void shouldApplyLongSerializationPolicy() {
      GsonBuilder builder = new GsonBuilder();
      builder.setLongSerializationPolicy(SerializationPolicy.LONG);
      Gson gson = builder.create();

      JsonElement jsonElement = gson.toJsonTree(1234567890L);
      assertThat(jsonElement, is(notNullValue()));
      assertThat(jsonElement.getAsJsonPrimitive().getAsLong(), is(1234567890L));
    }

    public void testSetFieldNamingPolicy() {
        Gson gson = new GsonBuilder()
               .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
               .create();
        assertEquals("lowerCaseWithUnderscores", gson.getFieldNamingPolicy());
    }

    @Test
    public void shouldApplyFieldNamingStrategy() {
        // given
        GsonBuilder builder = new GsonBuilder();
        FieldNamingStrategy fieldNamingStrategy = new FieldNamingStrategy() {
            @Override
            public String translateName(String name) {
                return "translated_" + name;
            }
        };
        builder.setFieldNamingStrategy(fieldNamingStrategy);

        // when
        Gson gson = builder.create();

        // then
        String translatedName = gson.fromJson("{\"name\": \"John\"}", Person.class).getName();
        assertEquals("translated_name", translatedName);
    }*/

    @Test
    public void testSetExclusionStrategies() {
        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(new TestExclusionStrategy());
        Gson gson = builder.create();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        jsonObject.addProperty("age", 25);
        String json = gson.toJson(jsonObject);
        assertEquals(json, "name=John&age=25");
    }

   /* @Test
    public void testSetPrettyPrinting() {
        // arrange
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(new Person("John", 30));
        // assert
        assertEquals("{\"name\":\"John\",\"age\":30}", json);
    }*/

    @Test
    public void testDisableHtmlEscaping() throws Exception {
        GsonBuilder builder = new GsonBuilder();
        builder.disableHtmlEscaping();
        Gson gson = builder.create();
        JsonElement jsonElement = gson.toJsonTree(new ArrayList<String>());
        //assertThat(jsonElement, is(new JsonPrimitive("['<', '>', '&','', '!', '\'']")));
    }

    @Test
    public void shouldSetDateFormat() {
        // Arrange
        GsonBuilder builder = new GsonBuilder();
        String expectedDateFormat = "yyyy-MM-dd";

        // Act
        builder.setDateFormat(expectedDateFormat);

        // Assert
        //assertEquals(expectedDateFormat, builder.getDateFormat());
    }

    public void testSetDateFormat() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        String json = gson.toJson(new Date());
        assertEquals("2013-11-12T13:15:30+01:00", json);
    }

   /* @Test
    public void registerTypeAdapter() {
        Gson gson = new GsonBuilder()
               .registerTypeAdapter(String.class, new StringDeserializer())
               .create();

        String json = gson.toJson("hello");
        assertEquals("{\"hello\":null}", json);
    }*/

    @Test
    public void testSerializeSpecialFloatingPointValues() {
        // Arrange
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeSpecialFloatingPointValues();

        // Act
        Gson gson = gsonBuilder.create();

        // Assert
        //assertEquals(true, gson.serializeSpecialFloatingPointValues());
    }

    @Test
    public void testCreate() {
        //given
        GsonBuilder builder = new GsonBuilder();
        //when
        Gson gson = builder.create();
        //then
        //assert
    }    
}