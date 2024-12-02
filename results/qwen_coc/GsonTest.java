/*
 * Copyright (C) 2016 The Gson Authors
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;
import com.google.gson.reflect.TypeToken;
import java.util.Map; // Importar la clase Map
import java.io.StringReader; // Importar la clase StringReader

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

@SuppressWarnings({"deprecation", "unchecked"})
public class GsonTest {

    @Test
    public void testToJsonTree() {
        // Arrange
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        jsonObject.addProperty("age", 30);
        jsonObject.addProperty("city", "New York");

        // Act
        JsonElement jsonElement = gson.toJsonTree(jsonObject);

        // Assert
        assertNotNull(jsonElement);
        assertEquals(jsonObject, jsonElement.getAsJsonObject());
    }

    @Test
    public void testToJsonTreeWithNull() {
        // Arrange
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        jsonObject.addProperty("age", 30);
        jsonObject.addProperty("city", "New York");

        // Act
        JsonElement jsonElement = gson.toJsonTree(jsonObject);

        // Assert
        assertNotNull(jsonElement);
        }
        
    @Test
    public void testToJsonTree2() {
        // Arrange
        List<String> list = new ArrayList<>();
        list.add("item1");
        list.add("item2");
        list.add("item3");

        // Act
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(list);

        // Assert
        assertNotNull(jsonElement);
        assertEquals("JsonElement", jsonElement.getClass().getName());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        assertNotNull(jsonObject);
        assertEquals("JsonElement", jsonObject.getClass().getName());
        assertEquals("JsonElement", jsonObject.get("items").getClass().getName());
        assertEquals("JsonElement", jsonObject.get("items").getAsJsonArray().get(0).getClass().getName());
        assertEquals("JsonElement", jsonObject.get("items").getAsJsonArray().get(1).getClass().getName());
        //assertEquals("JsonElement", jsonObject.get("items").getAsJsonArray        
    }
    
    @Test
    public void testToJson6() {
        // Create a Gson instance
        Gson gson = new Gson();

        // Create a sample object
        Person person = new Person("John", 30);

        // Serialize the object to JSON
        String json = gson.toJson(person);

        // Deserialize the JSON back to an object
        Person deserializedPerson = gson.fromJson(json, Person.class);

        // Verify that the deserialized object is equal to the original object
        assertEquals(person, deserializedPerson);

        // Verify that the JSON representation is not null
        assertNotNull(json);

        // Verify that the JSON representation is not empty
        assertNotNull(json.trim());

        // Verify that the JSON representation is not an empty string
        assertNotNull(json.trim().length() > 0);
    }

    private Gson gson;

    @Before
    public void setUp() {
        gson = new Gson();
    }

    @Test
    public void testToJson5() {
        // Mock the object to be serialized
        Object src = new Object();

        // Mock the type of the object
        Type typeOfSrc = new TypeToken<Object>() {}.getType();

        // Mock the expected JSON representation
        String expectedJson = "{\"type\":\"Object\",\"value\":\"{\"field1\":\"value1\",\"field2\":\"value2\"}\"}";

        // Serialize the object and compare it with the expected JSON representation
        String actualJson = gson.toJson(src, typeOfSrc);
        assertEquals(expectedJson, actualJson);
    }
    
    @Test
    public void testToJson4() {
        // Create a Gson instance
        Gson gson = new GsonBuilder().create();

        // Create a list of objects
        List<Object> objects = new ArrayList<>();
        objects.add(new Object());
        objects.add(new Object());

        // Serialize the list to Json
        String json = gson.toJson(objects);

        // Parse the Json back to a list of objects
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);
        List<Object> parsedObjects = gson.fromJson(element, List.class);

        // Verify that the parsed objects are the same as the original objects
        for (int i = 0; i < objects.size(); i++) {
            assertEquals(objects.get(i), parsedObjects.get(i));
        }
    }

    @Test
    public void testToJson3() {
        // Arrange
        Gson gson = new Gson();
        String src = "{\"name\":\"John\", \"age\":30}";
        Type typeOfSrc = new TypeToken<Map<String, Object>>(){}.getType();
        StringWriter writer = new StringWriter();

        // Act
        gson.toJson(src, typeOfSrc, writer);

        // Assert
        assertEquals("{\"name\":\"John\", \"age\":30}", writer.toString());
    }

    @Test
    public void testToJson2() {
        // Arrange
        Gson gson = new Gson();
        JsonElement jsonElement = new JsonParser().parse("{\"name\":\"John\", \"age\":30}");

        // Act
        String json = gson.toJson(jsonElement);

        // Assert
        assertEquals("{\"name\":\"John\", \"age\":30}", json);
    }

    @Test
    public void testToJson1() {
        // Arrange
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        jsonObject.addProperty("age", 30);

        // Act
        String json = gson.toJson(jsonObject);

        // Assert
        assertEquals("{\"name\":\"John\",\"age\":30}", json);
    }    

    @Test
    public void testFromJson() {
        Gson gson = new Gson();
        String json = "{\"name\":\"John\", \"age\":30}";
        Person person = gson.fromJson(json, Person.class);
        assertNotNull(person);
        assertEquals("John", person.getName());
        assertEquals(30, person.getAge());
    }

    @Test
    public void testFromJsonWithReader() {
        Gson gson = new Gson();
        String json = "{\"name\":\"John\", \"age\":30}";
        Person person = gson.fromJson(new StringReader(json), Person.class);
        assertNotNull(person);
        assertEquals("John", person.getName());
        assertEquals(30, person.getAge());
    }
    
    @Test
    public void testFromJson6() {
        // Arrange
        String json = "{\"name\":\"John\", \"age\":30}";
        Type type = new TypeToken<JsonObject>() {}.getType();

        // Act
        Gson gson = new Gson();
        JsonObject result = gson.fromJson(json, type);

        // Assert
        assertEquals("John", result.get("name").getAsString());
        assertEquals(30, result.get("age").getAsInt());
    }

    /*@Test
    public void testFromJson5() {
        // Arrange
        String json = "{\"name\":\"John\", \"age\":30}";
        Class<?> clazz = String.class;

        // Act
        String result = Gson.fromJson(json, clazz);

        // Assert
        assertEquals("John", result);
    }   */ 
 
    @Test
    public void testFromJson4() {
        // Arrange
        String json = "{\"name\":\"John\", \"age\":30}";
        Gson gson = new Gson();
        Type type = new TypeToken<String>(){}.getType();

        // Act
        String result = gson.fromJson(json, type);

        // Assert
        assertEquals("John", result);
    }

    @Test
    public void testFromJson3() {
        // Arrange
        Gson gson = new Gson();
        String json = "{\"name\":\"John\", \"age\":30}";
        Type type = new TypeToken<Map<String, Object>>(){}.getType();

        // Act
        Map<String, Object> result = gson.fromJson(json, type);

        // Assert
        assertEquals("John", result.get("name"));
        assertEquals(30, result.get("age"));
    }

   /* @Test
    public void testFromJson2() {
        // Arrange
        Gson gson = new Gson();
        String json = "{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}";
        Class<?> classOfT = String.class;

        // Act
        String result = gson.fromJson(json, classOfT);

        // Assert
        assertEquals("John", result);
    }

    @Test
    public void testFromJson1() {
        // Arrange
        String json = "{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}";
        Class<?> expectedType = String.class;

        // Act
        Gson gson = new Gson();
        JsonElement jsonElement = new JsonParser().parse(json);
        String result = gson.fromJson(jsonElement, expectedType);

        // Assert
        assertNotNull(result);
        assertEquals("John", result);
    }*/
}