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

import static org.junit.Assert.assertEquals;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.junit.Test;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

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

    private Gson gson = new Gson();

    @Test
    public void testToJsonTree() throws Exception {
        List<String> list = Arrays.asList("a", "b", "c");
        assertEquals(gson.toJsonTree(list), gson.toJsonTree(list));
    }

    @Test
    public void testToJson()
    {
    Object obj = new Object();
    // Your code to test toJson() method here
    }

    @Test
    public void testToJSON() {
        Gson gson = new Gson();
        String json = gson.toJson(1);
        assertEquals("1", json);

        json = gson.toJson(new Integer(1));
        assertEquals("1", json);

        json = gson.toJson(new Integer(1), Integer.class);
        assertEquals("1", json);

        json = gson.toJson(new Integer(1), new TypeToken<Integer>(){}.getType());
        assertEquals("1", json);

        json = gson.toJson(new Integer(1), new TypeToken<Number>(){}.getType());
        assertEquals("1", json);

        json = gson.toJson(new Integer(1), new TypeToken<Object>(){}.getType());
        assertEquals("1", json);

        json = gson.toJson(new Integer(1), new TypeToken<Object>(){}.getType());
        assertEquals("1", json);

        json = gson.toJson(new Integer(1), new TypeToken<Number>(){}.getType());
        assertEquals("1", json);

      }

    /*@Test
    public void testToJson2() {
        Collection<Foo> collection = new ArrayList<>();
        collection.add(new Foo("foo1"));
        collection.add(new Foo("foo2"));
        Gson gson = new Gson();
        String json = gson.toJson(collection);
        assertThat(json, is("[" +
                "{\"name\":\"foo1\"}" +
                ",{\"name\":\"foo2\"}" +
                "]"));
    }

    @Test
    public void testToJson3() throws IOException {
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJson(new Foo("Johanna"));
        assertTrue(jsonElement.isJsonObject());
    }

    @Test
    public void testToJSON10() {
      Gson gson = new Gson();
      String json = gson.toJson(new Foo("Bob", Arrays.asList("apple", "banana", "cherry")));
      assertEquals("[\"apple\",\"banana\",\"cherry\"]", json);
    }


    public String toJson(Object src)
    {
    return new Gson().toJson(src);
    }

    @Test
    public void testToJSON4() {
    Gson gson = new Gson();
    String json = gson.toJson(new Foo("Bob", Arrays.asList("apple", "banana", "cherry")));
    assertEquals("[\"apple\",\"banana\",\"cherry\"]", json);
    }*/

    public String toJson5(Object src)
    {
    return new Gson().toJson(src);
    }

   /* @Test
    public void testToJSON6() {
      // Given
      final JsonElement root = new JsonPrimitive(123);
      final StringWriter stringWriter = new StringWriter();

      // When
      gson.toJson(root, stringWriter);

      // Then
      assertThat(stringWriter.toString(), is("123"));
    }*/
    
    @Test
    public void testJson() throws IOException {
        Gson gson = new Gson();

        List<String> list = new ArrayList<String>();
        list.add("one");
        list.add("two");
        list.add("three");

        String json = gson.toJson(list);

        assertTrue(json.startsWith("["));
        assertTrue(json.endsWith("]"));
        assertTrue(json.contains("\"one\""));
        assertTrue(json.contains("\"two\""));
        assertTrue(json.contains("\"three\""));
    }

    @Test
    public void fromJsonShouldDeserializeAString() throws JsonSyntaxException {
        String json = "{\"name\":\"Tom\"}";
        Foo person = gson.fromJson(json, Foo.class);
        assertEquals("Tom", person.getName());
    }

    @Test
    public void fromJsonShouldDeserializeANumber() throws JsonSyntaxException {
        String json = "{\"number\":123}";
        Number number = gson.fromJson(json, Number.class);
        assertEquals(123, number.doubleValue());
    }

   /* @Test
    public void fromJsonShouldDeserializeAnArray() throws JsonSyntaxException {
        String json = "[1,2,3]";
        Integer[] numbers = gson.fromJson(json, new TypeToken<Integer[]>() {}.getType());
        assertEquals(3, numbers.length);
        assertEquals(1, numbers[0]);
        assertEquals(2, numbers[1]);
        assertEquals(3, numbers[2]);
    }*/

    @Test
    public void testFromJson() {
        Gson gson = new Gson();
        List<String> list = new ArrayList<>();
        list.add("foo");
        list.add("bar");
        String json = gson.toJson(list);
        assertEquals("[\"foo\",\"bar\"]", json);
    }

    /*@Test
    public void testFromJson2() throws Exception {
        String json = "{\"name\":\"Tom\",\"age\":25}";
        Gson gson = new Gson();
        Foo s = gson.fromJson(json, Foo.class);
        assertEquals("Tom", s.getName());
        assertEquals(25, s.getAge());
    }*/

    @Test
    public void test() throws Exception {
        Gson gson = new Gson();
        String json = "[1,2,3]";
        String expected = "[1,2,3]";
        assertEquals(expected, gson.fromJson(json, String.class));
    }

    /*@Test
    public void test2() throws Exception {
        String json = "{\"name\":\"John Doe\",\"age\":42}";
        Foo person = gson.fromJson(json, Foo.class);
        assertThat(person).isNotNull();
        assertThat(person.getName()).isEqualTo("John Doe");
        assertThat(person.getAge()).isEqualTo(42);
    }*/

    @Test
    public void testBasicJsonSerialization() throws IOException {
        String json = "{\"name\":\"James\",\"age\":32}";
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        assertEquals("James", jsonObject.get("name").getAsString());
        assertEquals(32, jsonObject.get("age").getAsInt());
    }

    @Test
    public void testNull() throws IOException {
        String json = "{\"name\":null}";
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        assertNull(jsonObject.get("name"));
    }

    @Test
    public void fromJsonTest() {
      Gson gson = new Gson();

      JsonElement json = new JsonParser().parse("{ \"name\" : \"value\" }");

      JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
      assertEquals("value", jsonObject.get("name").getAsString());
    }

    @Test
    public void fromJsonArrayTest() {
      Gson gson = new Gson();

      JsonElement json = new JsonParser().parse("[1,2,3,4,5]");

      JsonArray jsonArray = gson.fromJson(json, JsonArray.class);
      assertEquals(5, jsonArray.size());
    }
}