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

public class JsonObjectTest {
    /*@Test
    public void testAdd() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("name", "John");
        assertEquals("John", jsonObject.get("name"));
    }*/
    
    @Test
    public void remove() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "value");

        JsonElement element = jsonObject.remove("key");
        assertEquals("value", element.getAsString());
        assertNull(jsonObject.get("key"));
    }

    @Test
    public void testAddProperty() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        assertEquals("John", jsonObject.get("name"));
    }

    @Test
    public void testAddProperty2() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", 123);
        assertEquals(1, jsonObject.size());
        assertEquals("123", jsonObject.get("name").toString());
    }    
    
    @Test
    public void testAddProperty3() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", true);
       // assertEquals(true, jsonObject.getAsBoolean("name"));
    }

    @Test
    public void testAddProperty4() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", 'A');
        assertEquals("A", jsonObject.get("name").toString());
    }

   /* @Test
    public void testEntrySet() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("key1", "value1");
        jsonObject.put("key2", "value2");
        jsonObject.put("key3", "value3");

        Set<Map.Entry<String, String>> expectedEntries = new HashSet<>();
        expectedEntries.add(new AbstractMap.SimpleEntry<>("key1", "value1"));
        expectedEntries.add(new AbstractMap.SimpleEntry<>("key2", "value2"));
        expectedEntries.add(new AbstractMap.SimpleEntry<>("key3", "value3"));

        Set<Map.Entry<String, String>> actualEntries = jsonObject.entrySet();

        assertEquals(expectedEntries, actualEntries);
    }*/

    @Test
    public void has() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        jsonObject.addProperty("age", 30);
        jsonObject.addProperty("city", "New York");

        assertTrue(jsonObject.has("name"));
        assertTrue(jsonObject.has("age"));
        assertTrue(jsonObject.has("city"));
        assertFalse(jsonObject.has("address"));
    }

    @Test
    public void get() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        jsonObject.addProperty("age", 30);
        jsonObject.addProperty("city", "New York");

        assertEquals("John", jsonObject.get("name"));
        assertEquals(30, jsonObject.get("age"));
        assertEquals("New York", jsonObject.get("city"));
    } 

    @Test
    public void getAsJsonPrimitive() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        jsonObject.addProperty("age", 30);

        JsonPrimitive name = jsonObject.getAsJsonPrimitive("name");
        JsonPrimitive age = jsonObject.getAsJsonPrimitive("age");

        assertEquals("John", name.getAsString());
        assertEquals(30, age.getAsInt());
    }

    @Test
    public void getAsJsonArray() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        jsonObject.addProperty("age", 30);
        jsonObject.addProperty("city", "New York");

        JsonArray jsonArray = jsonObject.getAsJsonArray("name");
        assertEquals(1, jsonArray.size());
        assertEquals("John", jsonArray.get(0).getAsString());
    }

    @Test
    public void getAsJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        jsonObject.addProperty("age", 30);

        JsonObject result = jsonObject.getAsJsonObject("name");
        assertEquals("John", result.getAsString());
    }    
}
