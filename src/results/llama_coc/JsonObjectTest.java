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
import com.google.gson.JsonElement; 
import com.google.gson.JsonObject; 
import com.google.gson.JsonPrimitive;
import java.util.Set;

public class JsonObjectTest {

    /*@Test
    public void testAdd() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("name", "John");
        assertEquals("John", jsonObject.get("name"));
    }*/
    
    @Test
    public void testRemove() {
        // Arrange
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key1", "value1");
        jsonObject.addProperty("key2", "value2");

        // Act
        JsonElement removedElement = jsonObject.remove("key1");

        // Assert
        assertEquals("value1", removedElement.getAsString());
        assertEquals(1, jsonObject.size());
        assertFalse(jsonObject.has("key1"));
    }

   /* @Test
    public void testAddProperty() {
        // Create a new JsonObject instance
        JsonObject jsonObject = new JsonObject();

        // Add a property with a string value
        jsonObject.addProperty("name", "John Doe");

        // Verify that the property was added correctly
        assertEquals("John Doe", jsonObject.getProperty("name"));
    }

    @Test
    public void testAddProperty2() {
        // Create a new JsonObject instance
        JsonObject jsonObject = new JsonObject();

        // Add a property with a number value
        jsonObject.addProperty("age", 25);

        // Verify that the property was added correctly
        assertEquals(1, jsonObject.size());
        assertEquals("age", jsonObject.get("age").getName());
        assertEquals(25, jsonObject.get("age").getValue(), 0.0);
    }  

    @Test
    public void testAddProperty3() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", true);
        assertEquals(true, jsonObject.getBoolean("name"));
    } 

    @Test
    public void testAddProperty4() {
        // Create a new JsonObject instance
        JsonObject jsonObject = new JsonObject();

        // Add a property with a char value
        jsonObject.addProperty("name", 'J');

        // Check if the property was added correctly
        assertEquals("J", jsonObject.getProperty("name"));
    }     
    
    @Test
    public void testEntrySet() {
        // Arrange
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("key1", "value1");
        jsonObject.put("key2", "value2");
        jsonObject.put("key3", "value3");

        // Act
        Set<Map.Entry<String, String>> entrySet = jsonObject.entrySet();

        // Assert
        assertEquals(3, entrySet.size());
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>("key1", "value1")));
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>("key2", "value2")));
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>("key3", "value3")));
    }*/
    
    @Test
    public void testHas() {
        // Create a new JsonObject instance
        JsonObject jsonObject = new JsonObject();

        // Add some members to the JsonObject
        jsonObject.addProperty("name", "John");
        jsonObject.addProperty("age", 30);

        // Check if the "name" member exists
        assertTrue(jsonObject.has("name"));

        // Check if the "age" member exists
        assertTrue(jsonObject.has("age"));

        // Check if the "gender" member exists (should return false)
        assertFalse(jsonObject.has("gender"));
    }
    
    /*@Test
    public void testGet() {
        // Arrange
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("name", "John");
        jsonObject.put("age", 30);

        // Act
        String name = jsonObject.get("name");
        int age = jsonObject.get("age");

        // Assert
        assertEquals("John", name);
        assertEquals(30, age);
    }*/
    
    @Test
    public void testGetAsJsonPrimitive() {
        // Create a JsonObject instance
        JsonObject jsonObject = new JsonObject();

        // Add some members to the JsonObject
        jsonObject.addProperty("name", "John");
        jsonObject.addProperty("age", 30);
        jsonObject.addProperty("city", "New York");

        // Test the getAsJsonPrimitive method for each member
        assertEquals("John", jsonObject.getAsJsonPrimitive("name").getAsString());
        assertEquals(30, jsonObject.getAsJsonPrimitive("age").getAsInt());
        assertEquals("New York", jsonObject.getAsJsonPrimitive("city").getAsString());
    }
    
    @Test
    public void testGetAsJsonArray() {
        // Arrange
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        jsonObject.addProperty("age", 30);
        jsonObject.addProperty("city", "New York");

        // Act
        JsonArray jsonArray = jsonObject.getAsJsonArray("name");

        // Assert
        assertEquals(1, jsonArray.size());
        assertEquals("John", jsonArray.get(0).getAsString());
    }
    
    @Test
    public void testGetAsJsonObject() {
        // Arrange
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        jsonObject.addProperty("age", 30);

        // Act
        JsonObject result = jsonObject.getAsJsonObject("name");

        // Assert
        //assertEquals("John", result.getAsString("name"));
        assertNull(result.get("age"));
    }
}