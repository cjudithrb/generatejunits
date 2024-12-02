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
import com.google.gson.JsonElement; 
import com.google.gson.JsonPrimitive;

public class JsonArrayTest {

    @Test
    public void testAdd() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        String element = "test";

        // Act
        jsonArray.add(element);

        // Assert
        assertEquals(1, jsonArray.size());
        assertEquals(element, jsonArray.get(0));
    }

    @Test
    public void testAddAll() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        JsonArray otherArray = new JsonArray();
        otherArray.add("element1");
        otherArray.add("element2");

        // Act
        jsonArray.addAll(otherArray);

        // Assert
        assertEquals(2, jsonArray.size());
        assertEquals("element1", jsonArray.get(0));
        assertEquals("element2", jsonArray.get(1));
    }
    
    @Test
    public void testSize() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("element1");
        jsonArray.add("element2");
        jsonArray.add("element3");

        // Act
        int size = jsonArray.size();

        // Assert
        assertEquals(3, size);
    }
    
    /*@Test
    public void testIterator() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("element1");
        jsonArray.add("element2");
        jsonArray.add("element3");

        // Act
        Iterator<String> iterator = jsonArray.iterator();

        // Assert
        assertTrue(iterator.hasNext());
        assertEquals("element1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("element2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("element3", iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testGet() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("element1");
        jsonArray.add("element2");
        jsonArray.add("element3");

        // Act
        String element = jsonArray.get(1);

        // Assert
        assertEquals("element2", element);
    }*/
    
    @Test
    public void testGetAsNumber() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(10));
        jsonArray.add(new JsonPrimitive(20));

        // Act
        Number number = jsonArray.getAsNumber();

        // Assert
        assertEquals(10, number.intValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetAsNumberWithMultipleElements() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(10));
        jsonArray.add(new JsonPrimitive(20));

        // Act
        jsonArray.getAsNumber();
    }
    
    @Test(expected = ClassCastException.class)
    public void testGetAsNumberWithInvalidElement() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("10"));

        // Act
        jsonArray.getAsNumber();
    }
    
    @Test
    public void testGetAsString() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("Hello"));
        jsonArray.add(new JsonPrimitive("World"));

        // Act
        String result = jsonArray.getAsString();

        // Assert
        assertEquals("Hello", result);
    }
    
    @Test
    public void testGetAsDouble() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(1.0));

        // Act
        double result = jsonArray.getAsDouble();

        // Assert
        assertEquals(1.0, result, 0.0);
    }

    @Test(expected = ClassCastException.class)
    public void testGetAsDoubleWithInvalidPrimitive() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("hello"));

        // Act
        jsonArray.getAsDouble();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetAsDoubleWithMultipleElements() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(1.0));
        jsonArray.add(new JsonPrimitive(2.0));

        // Act
        jsonArray.getAsDouble();
    }
    
    @Test
    public void testGetAsBigDecimal() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("123.45"));

        // Act
        BigDecimal result = jsonArray.getAsBigDecimal();

        // Assert
        assertEquals(new BigDecimal("123.45"), result);
    }

    @Test(expected = ClassCastException.class)
    public void testGetAsBigDecimalWithNonPrimitive() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("123.45"));
        jsonArray.add(new JsonPrimitive("678.90"));

        // Act
        jsonArray.getAsBigDecimal();
    }
    
    @Test(expected = NumberFormatException.class)
    public void testGetAsBigDecimalWithInvalidBigDecimal() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("abc"));

        // Act
        jsonArray.getAsBigDecimal();
    }
    
    @Test
    public void testGetAsBigInteger() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(BigInteger.valueOf(1234567890123456789L)));

        // Act
        BigInteger result = jsonArray.getAsBigInteger();

        // Assert
        assertEquals(BigInteger.valueOf(1234567890123456789L), result);
    }

    @Test(expected = ClassCastException.class)
    public void testGetAsBigIntegerWithNonPrimitive() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("not a number"));

        // Act
        jsonArray.getAsBigInteger();
    }
    
    @Test(expected = NumberFormatException.class)
    public void testGetAsBigIntegerWithInvalidNumber() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("not a number"));

        // Act
        jsonArray.getAsBigInteger();
    }
    
    @Test
    public void testGetAsFloat() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(1.0f));

        // Act
        float result = jsonArray.getAsFloat();

        // Assert
        assertEquals(1.0f, result, 0.0f);
    }

    @Test(expected = ClassCastException.class)
    public void testGetAsFloatWithInvalidElement() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("string"));

        // Act
        jsonArray.getAsFloat();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetAsFloatWithMultipleElements() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(1.0f));
        jsonArray.add(new JsonPrimitive(2.0f));

        // Act
        jsonArray.getAsFloat();
    }
    
    @Test
    public void testGetAsLong() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(123L));

        // Act
        long result = jsonArray.getAsLong();

        // Assert
        assertEquals(123L, result);
    }

    @Test(expected = ClassCastException.class)
    public void testGetAsLongWithInvalidPrimitive() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("abc"));

        // Act
        jsonArray.getAsLong();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetAsLongWithMultipleElements() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(123L));
        jsonArray.add(new JsonPrimitive(456L));

        // Act
        jsonArray.getAsLong();
    }

    @Test
    public void testGetAsInt() {
        // Test case 1: Single element array
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive(10));
        assertEquals(10, array.getAsInt());

        // Test case 2: Multiple element array
        array = new JsonArray();
        array.add(new JsonPrimitive(10));
        array.add(new JsonPrimitive(20));
        try {
            array.getAsInt();
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected exception
        }

        // Test case 3: Invalid element type
        array = new JsonArray();
        array.add(new JsonPrimitive("10"));
        try {
            array.getAsInt();
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetAsShort() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(123));

        // Act
        short result = jsonArray.getAsShort();

        // Assert
        assertEquals(123, result);
    }

    @Test(expected = ClassCastException.class)
    public void testGetAsShortWithInvalidElement() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("abc"));

        // Act
        jsonArray.getAsShort();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetAsShortWithMultipleElements() {
        // Arrange
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(123));
        jsonArray.add(new JsonPrimitive(456));

        // Act
        jsonArray.getAsShort();
    }
    
    @Test
    public void testGetAsBoolean() {
        // Test case 1: Single element array with a boolean value
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(true));
        assertTrue(jsonArray.getAsBoolean());

        // Test case 2: Single element array with a non-boolean value
        jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("true"));
        try {
            jsonArray.getAsBoolean();
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected exception
        }

        // Test case 3: Single element array with a null value
        /*jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(null));
        try {
            jsonArray.getAsBoolean();
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected exception
        }
        
                // Test case 4: Single element array with a boolean value
        jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(false));
        assertFalse(jsonArray.getAsBoolean());*/
    }
}