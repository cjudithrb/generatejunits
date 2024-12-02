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

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.gson.Gson; 
import com.google.gson.reflect.TypeToken; // Importa TypeToken import java.lang.reflect.Type;
import java.lang.reflect.Type; // Importa Type
import java.util.Collection;
import java.util.ArrayList; // Importa ArrayList
import java.io.StringWriter; // Importa StringWriter

class Foo {
    private String name;

    public Foo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
@SuppressWarnings("deprecation")
public class GsonTest {

  /*  @Test
    public void testToJsonTree() {
        // Arrange
        Gson gson = new Gson();
        MyObject myObject = new MyObject();

        // Act
        JsonElement jsonElement = gson.toJsonTree(myObject);

        // Assert
        assertEquals("Expected JsonElement", jsonElement.toString(), "Expected JsonElement");
    }*/
    
    @Test
    public void testToJsonTree2() {
        // Arrange
        Gson gson = new Gson();
        Type typeOfSrc = new TypeToken<Collection<Foo>>(){}.getType();
        Collection<Foo> src = new ArrayList<>();
        src.add(new Foo("foo"));
        src.add(new Foo("bar"));

        // Act
        JsonElement jsonElement = gson.toJsonTree(src, typeOfSrc);

        // Assert
        assertEquals("foo", jsonElement.getAsJsonArray().get(0).getAsString());
        assertEquals("bar", jsonElement.getAsJsonArray().get(1).getAsString());
    }
    
   /* @Test
    public void testToJson() {
        // Arrange
        Gson gson = new Gson();
        String expectedJson = "{\"name\":\"John\",\"age\":30}";

        // Act
        String actualJson = gson.toJson(new Foo("John", 30));

        // Assert
        assertEquals(expectedJson, actualJson);
    }    */

    @Test
    public void testToJson2() {
        // Arrange
        Gson gson = new Gson();
        Type typeOfSrc = new TypeToken<Collection<Foo>>(){}.getType();
        Collection<Foo> src = new ArrayList<>();
        src.add(new Foo("foo"));
        src.add(new Foo("bar"));

        // Act
        String json = gson.toJson(src, typeOfSrc);

        // Assert
        assertEquals("[{\"name\":\"foo\"},{\"name\":\"bar\"}]", json);
    }

    /*@Test
    public void testToJson3() {
        // Arrange
        Gson gson = new Gson();
        String expectedJson = "{\"name\":\"John\",\"age\":30}";

        // Act
        String actualJson = gson.toJson(new Foo("John", 30));

        // Assert
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testToJson4() {
        // Arrange
        Gson gson = new Gson();
        String expectedJson = "{\"name\":\"John\", \"age\":30}";

        // Act
        String actualJson = gson.toJson(new Foo("John", 30));

        // Assert
        assertEquals(expectedJson, actualJson);
    }*/

    @Test
    public void testToJson5() {
        // Arrange
        Gson gson = new Gson();
        Type typeOfSrc = new TypeToken<String>() {}.getType();
        String src = "Hello, World!";
        StringWriter writer = new StringWriter();

        // Act
        gson.toJson(src, typeOfSrc, writer);

        // Assert
        assertEquals(src, writer.toString());
    }

    @Test
    public void testToJson6() {
        Gson gson = new Gson();
        JsonElement jsonElement = new JsonParser().parse("{\"name\":\"John\", \"age\":30}");
        String json = gson.toJson(jsonElement);
        assertEquals("{\"name\":\"John\", \"age\":30}", json);
    }

    @Test
    public void testToJson7() {
        // Arrange
        JsonElement jsonElement = new JsonPrimitive("Hello, World!");
        StringWriter writer = new StringWriter();

        // Act
        Gson gson = new Gson();
        gson.toJson(jsonElement, writer);

        // Assert
        assertEquals("{\"value\":\"Hello, World!\"}", writer.toString());
    }

    @Test
    public void testToJson8() {
        Gson gson = new Gson();
        JsonObject jsonElement = new JsonObject();
        jsonElement.addProperty("name", "John");
        jsonElement.addProperty("age", 30);
        String json = gson.toJson(jsonElement);
        assertEquals("{\"name\":\"John\",\"age\":30}", json);
    }    
    
  /*  @Test
    public void testFromJson() {
        // Arrange
        String json = "{\"name\":\"John\", \"age\":30}";
        Class<T> classOfT = Person.class;

        // Act
        Person person = Gson.fromJson(json, classOfT);

        // Assert
        assertEquals("John", person.getName());
        assertEquals(30, person.getAge());
    }

    @Test
    public void testFromJson2() {
        // Arrange
        String json = "{\"name\":\"John\", \"age\":30}";
        Type typeOfT = new TypeToken<JsonObject>() {}.getType();

        // Act
        JsonObject result = Gson.fromJson(json, typeOfT);

        // Assert
        assertEquals("John", result.get("name").getAsString());
        assertEquals(30, result.get("age").getAsInt());
    }

    @Test
    public void testFromJson3() {
        // Arrange
        Gson gson = new Gson();
        String json = "{\"name\":\"John\", \"age\":30}";
        Type type = new TypeToken<Person>(){}.getType();
        Person expected = new Person("John", 30);

        // Act
        Person actual = gson.fromJson(json, type);

        // Assert
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getAge(), actual.getAge());
    }
 
    @Test
    public void testFromJson4() {
        Gson gson = new Gson();
        String json = "{\"name\":\"John\", \"age\":30}";
        Foo person = gson.fromJson(json, Foo.class);
        assertEquals("John", person.getName());
        assertEquals(30, person.getAge());
    } 
    
    @Test
    public void testFromJson5() {
        // Arrange
        Gson gson = new Gson();
        String json = "{\"name\":\"John\", \"age\":30}";
        Type type = new TypeToken<Foo>(){}.getType();
        Foo expected = new Foo("John", 30);

        // Act
        Foo actual = gson.fromJson(json, type);

        // Assert
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getAge(), actual.getAge());
    }

    @Test
    public void testFromJson6() {
        // Arrange
        Gson gson = new Gson();
        String json = "{\"name\":\"John\", \"age\":30}";
        Type type = new TypeToken<Foo>(){}.getType();

        // Act
        Foo person = gson.fromJson(json, type);

        // Assert
        assertEquals("John", person.getName());
        assertEquals(30, person.getAge());
    }*/
    
}