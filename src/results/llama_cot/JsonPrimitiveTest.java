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
import static org.junit.Assert.assertThrows;
import com.google.gson.JsonPrimitive; 
import java.math.BigDecimal;
import java.math.BigInteger; // Importa BigInteger

public class JsonPrimitiveTest {

    @Test
    public void testIsBoolean() {
        // Arrange
        JsonPrimitive primitive = new JsonPrimitive(true);

        // Act
        boolean result = primitive.isBoolean();

        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testGetAsBoolean() {
        JsonPrimitive primitive = new JsonPrimitive("true");
        boolean result = primitive.getAsBoolean();
        assertEquals(true, result);
    }

    @Test
    public void testIsNumber() {
        // Arrange
        JsonPrimitive primitive = new JsonPrimitive(123);
        JsonPrimitive primitive2 = new JsonPrimitive("123");
        JsonPrimitive primitive3 = new JsonPrimitive("123.456");
        JsonPrimitive primitive4 = new JsonPrimitive("abc");
        JsonPrimitive primitive5 = new JsonPrimitive(true);
        JsonPrimitive primitive6 = new JsonPrimitive(false);
        //JsonPrimitive primitive7 = new JsonPrimitive(null);

        // Act
        boolean result1 = primitive.isNumber();
        boolean result2 = primitive2.isNumber();
        boolean result3 = primitive3.isNumber();
        boolean result4 = primitive4.isNumber();
        boolean result5 = primitive5.isNumber();
        boolean result6 = primitive6.isNumber();
        //boolean result7 = primitive7.isNumber();

        // Assert
        assertEquals(true, result1);
        assertEquals(true, result2);
        assertEquals(true, result3);
        assertEquals(false, result4);
        assertEquals(false, result5);
        assertEquals(false, result6);
        //assertEquals(false, result7);
    }

    @Test
    public void testGetAsNumber() {
        // Arrange
        JsonPrimitive primitive = new JsonPrimitive("123");
        JsonPrimitive primitive2 = new JsonPrimitive("456");

        // Act
        Number number = primitive.getAsNumber();
        Number number2 = primitive2.getAsNumber();

        // Assert
        assertEquals(123, number.intValue());
        assertEquals(456, number2.intValue());
    }

    @Test
    public void testGetAsNumberWithUnsupportedType() {
        // Arrange
        JsonPrimitive primitive = new JsonPrimitive("abc");

        // Act & Assert
        assertThrows(NumberFormatException.class, () -> primitive.getAsNumber());
    }

    @Test
    public void testIsString() {
        // Arrange
        JsonPrimitive primitive = new JsonPrimitive("Hello, World!");

        // Act
        boolean result = primitive.isString();

        // Assert
        assertTrue(result);
    }

    @Test
    public void testGetAsString_String() {
        JsonPrimitive primitive = new JsonPrimitive("Hello, World!");
        assertEquals("Hello, World!", primitive.getAsString());
    }

    @Test
    public void testGetAsString_Number() {
        JsonPrimitive primitive = new JsonPrimitive(42);
        assertEquals("42", primitive.getAsString());
    }

    @Test
    public void testGetAsString_Boolean() {
        JsonPrimitive primitive = new JsonPrimitive(true);
        assertEquals("true", primitive.getAsString());
    }

    /*@Test
    public void testGetAsString_UnexpectedType() {
        JsonPrimitive primitive = new JsonPrimitive(new Object());
        assertThrows(AssertionError.class, () -> primitive.getAsString());
    }*/

    @Test
    public void testGetAsDoubleNumber() {
        JsonPrimitive primitive = new JsonPrimitive(123.456);
        double result = primitive.getAsDouble();
        assertEquals(123.456, result, 0.0);
    }

    @Test
    public void testGetAsDoubleString() {
        JsonPrimitive primitive = new JsonPrimitive("123.456");
        double result = primitive.getAsDouble();
        assertEquals(123.456, result, 0.0);
    }

    @Test
    public void testGetAsDoubleInvalid() {
        JsonPrimitive primitive = new JsonPrimitive("invalid");
        assertThrows(NumberFormatException.class, () -> primitive.getAsDouble());
    }  

    @Test
    public void testGetAsBigDecimal() {
        JsonPrimitive primitive = new JsonPrimitive("123.45");
        BigDecimal expected = new BigDecimal("123.45");
        assertEquals(expected, primitive.getAsBigDecimal());
    }

    @Test
    public void testGetAsBigInteger() {
        JsonPrimitive primitive = new JsonPrimitive("123");
        BigInteger expected = new BigInteger("123");
        BigInteger actual = primitive.getAsBigInteger();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAsBigIntegerWithInvalidValue() {
        JsonPrimitive primitive = new JsonPrimitive("abc");
        assertThrows(NumberFormatException.class, () -> primitive.getAsBigInteger());
    }

    @Test
    public void testGetAsFloatNumber() {
        JsonPrimitive primitive = new JsonPrimitive(123.456);
        float result = primitive.getAsFloat();
        assertEquals(123.456f, result, 0.0f);
    }

    @Test
    public void testGetAsFloatString() {
        JsonPrimitive primitive = new JsonPrimitive("123.456");
        float result = primitive.getAsFloat();
        assertEquals(123.456f, result, 0.0f);
    }

    @Test
    public void testGetAsFloatNonNumber() {
        JsonPrimitive primitive = new JsonPrimitive("abc");
        assertThrows(NumberFormatException.class, () -> primitive.getAsFloat());
    }

    @Test
    public void testGetAsLong() {
        JsonPrimitive primitive = new JsonPrimitive(123L);
        assertEquals(123L, primitive.getAsLong());
    }

    @Test
    public void testGetAsShort() {
        JsonPrimitive primitive = new JsonPrimitive(123);
        assertEquals(123, primitive.getAsShort());
    }

    @Test
    public void testGetAsInt() {
        // Arrange
        JsonPrimitive primitive = new JsonPrimitive(123);
        int expected = 123;

        // Act
        int actual = primitive.getAsInt();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAsIntWithNumber() {
        // Arrange
        JsonPrimitive primitive = new JsonPrimitive(123.456);
        int expected = 123;

        // Act
        int actual = primitive.getAsInt();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAsIntWithString() {
        // Arrange
        JsonPrimitive primitive = new JsonPrimitive("123");
        int expected = 123;

        // Act
        int actual = primitive.getAsInt();

        // Assert
        assertEquals(expected, actual);
    }
}