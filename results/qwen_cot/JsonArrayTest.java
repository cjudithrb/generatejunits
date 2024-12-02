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

public class JsonArrayTest {
    @Test
    public void testAdd() {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("element1");
        jsonArray.add("element2");
        assertEquals(2, jsonArray.size());
        assertEquals("element1", jsonArray.get(0));
        assertEquals("element2", jsonArray.get(1));
    }
    
  /*  @Test
    public void testAddAll() {
        JsonArray array = new JsonArray();
        array.addAll(new String[]{"a", "b", "c"});
        assertEquals(3, array.size());
        assertEquals("a", array.get(0));
        assertEquals("b", array.get(1));
        assertEquals("c", array.get(2));
    }*/

    @Test
    public void testSize() {
        JsonArray jsonArray = new JsonArray();
        assertEquals(0, jsonArray.size());
    }

   /* @Test
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
    }*/

    @Test
    public void testGet() {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("element1");
        jsonArray.add("element2");
        jsonArray.add("element3");

        assertEquals("element1", jsonArray.get(0));
        assertEquals("element2", jsonArray.get(1));
        assertEquals("element3", jsonArray.get(2));

        try {
            jsonArray.get(3);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            jsonArray.get(-1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void getAsNumber_singleElement() {
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive(123));
        assertEquals(123, array.getAsNumber());
    }

    @Test
    public void getAsNumber_multipleElements() {
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive(123));
        array.add(new JsonPrimitive(456));
        assertThrows(IllegalStateException.class, () -> array.getAsNumber());
    }

    @Test
    public void getAsNumber_invalidType() {
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive("abc"));
        assertThrows(ClassCastException.class, () -> array.getAsNumber());
    }

    @Test
    public void getAsString_singleElement() {
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive("Hello"));
        String result = array.getAsString();
        assertEquals("Hello", result);
    }

    @Test
    public void getAsString_multipleElements() {
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive("Hello"));
        array.add(new JsonPrimitive("World"));
        assertThrows(IllegalStateException.class, () -> array.getAsString());
    }

   /* @Test
    public void getAsString_nullElement() {
        JsonArray array = new JsonArray();
        array.add(null);
        assertThrows(ClassCastException.class, () -> array.getAsString());
    }*/

    /*@Test
    public void getAsDouble_singleElementArray() {
        JsonArray array = new JsonArray(new JsonPrimitive(1.0));
        double result = array.getAsDouble();
        assertEquals(1.0, result, 0.0);
    }*/

   /* @Test
    public void getAsDouble_multipleElementArray() {
        JsonArray array = new JsonArray(new JsonPrimitive(1.0), new JsonPrimitive(2.0));
        assertThrows(IllegalStateException.class, () -> array.getAsDouble());
    }

    @Test
    public void getAsDouble_invalidElement() {
        JsonArray array = new JsonArray(new JsonPrimitive("1.0"));
        assertThrows(ClassCastException.class, () -> array.getAsDouble());
    }*/

    @Test
    public void getAsBigDecimal_singleElement() {
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive("123.45"));
        BigDecimal expected = new BigDecimal("123.45");
        assertEquals(expected, array.getAsBigDecimal());
    }

    @Test
    public void getAsBigDecimal_multipleElements() {
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive("123.45"));
        array.add(new JsonPrimitive("678.90"));
        assertThrows(IllegalStateException.class, () -> array.getAsBigDecimal());
    }

    @Test
    public void getAsBigDecimal_invalidElement() {
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive("abc"));
        assertThrows(NumberFormatException.class, () -> array.getAsBigDecimal());
    }

   /* @Test
    public void getAsBigDecimal_nullElement() {
        JsonArray array = new JsonArray();
        array.add(null);
        assertThrows(ClassCastException.class, () -> array.getAsBigDecimal());
    }*/

    @Test
    public void getAsBigInteger_singleElement() {
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive(BigInteger.valueOf(10)));

        BigInteger expected = BigInteger.valueOf(10);
        BigInteger actual = array.getAsBigInteger();

        assertEquals(expected, actual);
    }

    @Test
    public void getAsBigInteger_multipleElements() {
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive(BigInteger.valueOf(10)));
        array.add(new JsonPrimitive(BigInteger.valueOf(20)));

        assertThrows(IllegalStateException.class, () -> array.getAsBigInteger());
    }

    @Test
    public void getAsBigInteger_invalidElement() {
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive("10"));

        assertThrows(ClassCastException.class, () -> array.getAsBigInteger());
    }

    @Test
    public void getAsBigInteger_invalidFormat() {
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive("10.5"));

        assertThrows(NumberFormatException.class, () -> array.getAsBigInteger());
    }
/*
    @Test
    public void getAsFloat_singleElement() {
        JsonArray array = new JsonArray(new JsonPrimitive(1.0f));
        float result = array.getAsFloat();
        assertEquals(1.0f, result, 0.0f);
    }
 
    @Test
    public void getAsFloat_multipleElements() {
        JsonArray array = new JsonArray(new JsonPrimitive(1.0f), new JsonPrimitive(2.0f));
        assertThrows(IllegalStateException.class, () -> array.getAsFloat());
    }

   @Test
    public void getAsFloat_invalidType() {
        JsonArray array = new JsonArray(new JsonPrimitive("1.0"));
        assertThrows(ClassCastException.class, () -> array.getAsFloat());
    }*/

  /*  @Test
    public void getAsLong_singleElement() {
        JsonArray array = new JsonArray(new JsonPrimitive(123));
        assertEquals(123L, array.getAsLong());
    }

    @Test
    public void getAsLong_multipleElements() {
        JsonArray array = new JsonArray(new JsonPrimitive(123), new JsonPrimitive(456));
        assertThrows(IllegalStateException.class, () -> array.getAsLong());
    }

    @Test
    public void getAsLong_invalidType() {
        JsonArray array = new JsonArray(new JsonPrimitive("abc"));
        assertThrows(ClassCastException.class, () -> array.getAsLong());
    }*/

    @Test
    public void getAsInt_singleElementArray() {
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive(10));
        int result = array.getAsInt();
        assertEquals(10, result);
    }

    @Test
    public void getAsInt_multipleElementsArray() {
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive(10));
        array.add(new JsonPrimitive(20));
        assertThrows(IllegalStateException.class, () -> array.getAsInt());
    }

    @Test
    public void getAsInt_invalidElement() {
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive("10"));
        assertThrows(ClassCastException.class, () -> array.getAsInt());
    }

    /*@Test
    public void getAsShort_singleElement() {
        JsonArray array = new JsonArray(new JsonPrimitive(1));
        short result = array.getAsShort();
        assertEquals(1, result);
    }

    @Test
    public void getAsShort_multipleElements() {
        JsonArray array = new JsonArray(new JsonPrimitive(1), new JsonPrimitive(2));
        assertThrows(IllegalStateException.class, () -> array.getAsShort());
    }

    @Test
    public void getAsShort_invalidType() {
        JsonArray array = new JsonArray(new JsonPrimitive("1"));
        assertThrows(ClassCastException.class, () -> array.getAsShort());
    }*/

    @Test
    public void getAsBoolean() {
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive("true"));
        boolean result = array.getAsBoolean();
        assertEquals(true, result);
    }
}