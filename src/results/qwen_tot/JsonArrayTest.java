/*
 * Copyright (C) 2011 Google Inc.
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
import static org.junit.Assert.assertThrows;

import java.math.BigDecimal; // Importar BigDecimal
import java.math.BigInteger; // Importar BigInteger
import java.util.Iterator;
import com.google.gson.JsonArray; 
import java.util.Arrays; 

public class JsonArrayTest {

    @Test
    public void testSizeWithEmptyArray() {
        JsonArray jsonArray = new JsonArray();
        assertEquals(0, jsonArray.size());
    }

    @Test
    public void testSizeWithSingleElementArray() {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("element");
        assertEquals(1, jsonArray.size());
    }

    @Test
    public void testSizeWithMultipleElementsArray() {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("element1");
        jsonArray.add("element2");
        assertEquals(2, jsonArray.size());
    }
    
    /*@Test
    public void testIteratorReturnsExpectedValue() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("element1");
        jsonArray.add("element2");
        jsonArray.add("element3");

        // Act
        Iterator<String> iterator = jsonArray.iterator();

        // Assert
        assertEquals("element1", iterator.next());
        assertEquals("element2", iterator.next());
        assertEquals("element3", iterator.next());
        assertFalse(iterator.hasNext());
    }*/
    
    @Test
    void testGet() {
        // Opción 1: Verificar el valor de retorno esperado del método
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("element1");
        jsonArray.add("element2");
        assertEquals("element1", jsonArray.get(0));
        assertEquals("element2", jsonArray.get(1));
        assertEquals("element2", jsonArray.get(1));
        assertEquals("element2", jsonArray.get(1));
        assertEquals("element2", jsonArray.get(1));
    }
    
    /*@Test
    public void testGetAsNumber() {
        // Opción 1: Verificar el valor de retorno esperado del método
        JsonArray jsonArray = new JsonArray(Arrays.asList(new JsonPrimitive(1), new JsonPrimitive(2), new JsonPrimitive(3)));
        assertEquals(1, jsonArray.getAsNumber(0).getAsInt());
        assertEquals(2, jsonArray.getAsNumber(1).getAsInt());
        assertEquals(3, jsonArray.getAsNumber(2).getAsInt());
    }
    
    @Test
    public void testGetAsStringSingleElement() {
        // Opción 1: Verificar el valor de retorno esperado del método
        List<JsonElement> elements = Arrays.asList(new JsonPrimitive("hello"));
        JsonArray jsonArray = new JsonArray(elements);
        assertEquals("hello", jsonArray.getAsString());
    }
    
    @Test
    public void testGetAsFloat() {
        // Opción 1: Verificar el valor de retorno esperado del método
        JsonArray jsonArray = new JsonArray(Arrays.asList(new JsonPrimitive(1.0f), new JsonPrimitive(2.0f), new JsonPrimitive(3.0f)));
        assertEquals(1.0f, jsonArray.getAsFloat(0), 0.0f);
        assertEquals(2.0f, jsonArray.getAsFloat(1), 0.0);
    }
    
    @Test
    public void testGetAsLong() {
        // Opción 1: Verificar el valor de retorno esperado del método
        JsonArray jsonArray = new JsonArray(Arrays.asList(new JsonPrimitive(10L)));
        assertEquals(10L, jsonArray.getAsLong());
    }
    
    @Test
    public void testGetAsIntSingleElement() {
        // Arrange
        JsonNodeFactory factory = new JsonNodeFactory(false);
        ArrayNode array = factory.arrayNode();
        array.add(factory.numberNode(42));

        // Act
        int result = array.getAsInt();
    }
    
    @Test
    public void testGetAsShort() {
        // Opción 1: Verificar el valor de retorno esperado del método
        JsonArray jsonArray = new JsonArray(Arrays.asList(new JsonPrimitive(123)));
        assertEquals(123, jsonArray.getAsShort(0));

        // Opción 2: Evaluar el comportamiento del método ante diferentes entradas
        jsonArray = new JsonArray(Arrays.asList(new JsonPrimitive("abc")));
        assertThrows(ClassCastException.class, () -> jsonArray.getAsShort(0));
    }
    
    @Test
    public void testGetAsBoolean() {
        // Opción 1: Verificar el valor de retorno esperado del método
        JsonArray jsonArray = new JsonArray(Arrays.asList(new JsonPrimitive(true)));
        assertEquals(true, jsonArray.getAsBoolean());

        jsonArray = new JsonArray(Arrays.asList(new JsonPrimitive(false)));
        assertEquals(false, jsonArray.getAsBoolean());
    }*/
    
}