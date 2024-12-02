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
   /* @Test
    public void testAddWithValidInput() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("name", "John Doe");
        assertEquals("John Doe", jsonObject.get("name"));
    }

    @Test
    public void testAddWithInvalidInput() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("name", 123);
        assertEquals("name", jsonObject.get("name"));
    }*/
    
    @Test
    public void testRemoveValidProperty() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "value");

        JsonElement removedElement = jsonObject.remove("key");

        assertEquals("value", removedElement.getAsString());
        assertEquals(0, jsonObject.entrySet().size());
    }

    @Test
    public void testRemoveNonexistentProperty() {
        JsonObject jsonObject = new JsonObject();

        JsonElement removedElement = jsonObject.remove("nonexistent");

        assertNull(removedElement);
    }
    
    /*@Test
    public void testAddPropertyWithValidInput() {
        JsonObject jsonObject = new JsonObject();
        String propertyName = "name";
        String propertyValue = "John Doe";
        jsonObject.addProperty(propertyName, propertyValue);

        assertEquals(propertyValue, jsonObject.getProperty(propertyName));
    }

    @Test
    public void testAddPropertyWithInvalidInput() {
        JsonObject jsonObject = new JsonObject();
        String propertyName = "name";
        String propertyValue = null;
        jsonObject.addProperty(propertyName, propertyValue);

        assertNull(jsonObject.getProperty(propertyName));
    }*/
    
    @Test
    public void testAddPropertyWithValidInput2() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("age", 25);
        assertEquals(1, jsonObject.size());
        assertEquals("age", jsonObject.get("age").toString());
    }

    @Test
    public void testAddPropertyWithInvalidInput2() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("age", "25");
        assertEquals(1, jsonObject.size());
        assertEquals("age", jsonObject.get("age").toString());
    }
    
    @Test
    public void testAddPropertyWithValidInput8() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", true);
        assertEquals(true, jsonObject.get("key").getAsBoolean());
    }

    @Test
    public void testAddPropertyWithInvalidInput3() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "invalid");
        assertEquals(null, jsonObject.get("key"));
    }
    
   /* @Test
    public void testAddPropertyWithValidInput4() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", 'J');
        assertEquals('J', jsonObject.getProperty("name"));
    }

    @Test
    public void testAddPropertyWithInvalidInput5() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", 'A');
        assertEquals('A', jsonObject.getProperty("name"));
    }

    @Test
    public void testAddPropertyWithNullInput() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", null);
        assertEquals(null, jsonObject.getProperty("name"));
    }
    
    @Test
    public void testEntrySet() {
        // Define el resultado esperado para diferentes entradas del método.
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key1", "value1");
        jsonObject.put("key2", "value2");

        // Asegúrate de incluir tanto los casos típicos como los casos límite.
        Set<String> expectedSet1 = jsonObject.entrySet().stream().map(entry -> entry.getKey()).collect(java.util.stream.Collectors.toSet());
        assertEquals(expectedSet1, jsonObject.entrySet());
    }*/
    
    @Test
    public void testHasWithValidName() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        boolean result = jsonObject.has("name");
        assertEquals(true, result);
    }

    @Test
    public void testHasWithInvalidName() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        boolean result = jsonObject.has("age");
        assertEquals(false, result);
    }

    @Test
    public void testHasWithNullName() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        boolean result = jsonObject.has(null);
        assertEquals(false, result);
    }
    
    /*@Test
    public void testGetWithValidName() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("name", "John");
        String expectedValue = "John";
        String actualValue = jsonObject.get("name");
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetWithInvalidName() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("name", "John");
        assertNull(jsonObject.get("age"));
    }

    @Test
    public void testGetWithNullName() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("name", "John");
        assertNull(jsonObject.get(null));
    }
    
    @Test
    public void testGetAsJsonArray() {
        // Define el resultado esperado para diferentes entradas del método.
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("name", "John");
        jsonObject.put("age", 30);
        jsonObject.put("city", "New York");

        // Caso típico: entrada válida
        JsonArray jsonArray = jsonObject.getAsJsonArray("name");
        assertEquals("[\"John\"]", jsonArray.toString());

        // Caso límite: entrada inválida
        jsonArray = jsonObject.getAsJsonArray("invalidKey");
        assertEquals("[]", jsonArray.toString());
    }*/

    @Test
    public void testGetAsJsonObject() {
        // Define el resultado esperado para diferentes entradas del método.
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        jsonObject.addProperty("age", 30);

        // Caso típico
        JsonObject result = jsonObject.getAsJsonObject("name");
        assertEquals("John", result.getAsString());

        // Caso límite
        result = jsonObject.getAsJsonObject("age");
        assertEquals(30, result.getAsInt());

        // Caso de entrada inválida
        result = jsonObject.getAsJsonObject("gender");
        assertEquals(null, result);
    }    
}