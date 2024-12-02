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

@SuppressWarnings({"deprecation", "unchecked"})
public class JsonPrimitiveTest {

    @Test
    public void testIsBoolean() {
        // Test case 1: Primitive containing a boolean value
        JsonPrimitive booleanPrimitive = new JsonPrimitive(true);
        assertTrue(booleanPrimitive.isBoolean());

        // Test case 2: Primitive containing a string value
        JsonPrimitive stringPrimitive = new JsonPrimitive("true");
        assertFalse(stringPrimitive.isBoolean());

        // Test case 3: Primitive containing a number value
        JsonPrimitive numberPrimitive = new JsonPrimitive(1);
        assertFalse(numberPrimitive.isBoolean());

        // Test case 4: Primitive containing a null value
        //JsonPrimitive nullPrimitive = new JsonPrimitive(null);
        //assertFalse(nullPrimitive.isBoolean());
    }
    
    @Test
    public void testGetAsBoolean() {
        // Test case 1: Primitive is a boolean
        JsonPrimitive booleanPrimitive = new JsonPrimitive(true);
        boolean result = booleanPrimitive.getAsBoolean();
        assertTrue(result);

        // Test case 2: Primitive is a string that represents a boolean
        JsonPrimitive stringPrimitive = new JsonPrimitive("true");
        result = stringPrimitive.getAsBoolean();
        assertTrue(result);

        // Test case 3: Primitive is a string that does not represent a boolean
        stringPrimitive = new JsonPrimitive("false");
        result = stringPrimitive.getAsBoolean();
        assertTrue(result);

        // Test case 4: Primitive is a string that does not represent a boolean and is not a boolean
        stringPrimitive = new JsonPrimitive("123");
        result = stringPrimitive.getAsBoolean();
        assertFalse(result);
    }

    @Test
    public void testIsNumber() {
        // Test case 1: Primitive containing a number
        JsonPrimitive primitive1 = new JsonPrimitive(123);
        assertTrue(primitive1.isNumber());

        // Test case 2: Primitive containing a string
        JsonPrimitive primitive2 = new JsonPrimitive("123");
        assertFalse(primitive2.isNumber());

        // Test case 3: Primitive containing a boolean
        JsonPrimitive primitive3 = new JsonPrimitive(true);
        assertFalse(primitive3.isNumber());

        // Test case 4: Primitive containing a null value
        //JsonPrimitive primitive4 = new JsonPrimitive(null);
        //assertFalse(primitive4.isNumber());
    }

    @Test
    public void testGetAsNumber() {
        // Test with a number
        JsonPrimitive primitive = new JsonPrimitive(123);
        assertEquals(123, primitive.getAsNumber());

        // Test with a string
        primitive = new JsonPrimitive("123");
        assertEquals(123, primitive.getAsNumber());

        // Test with a non-number string
        primitive = new JsonPrimitive("abc");
        try {
            primitive.getAsNumber();
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test
    public void testIsString() {
        // Test case 1: Primitive with string value
        JsonPrimitive primitive1 = new JsonPrimitive("Hello, World!");
        assertTrue(primitive1.isString());

        // Test case 2: Primitive with non-string value
        JsonPrimitive primitive2 = new JsonPrimitive(123);
        assertFalse(primitive2.isString());

        // Test case 3: Primitive with null value
        //JsonPrimitive primitive3 = new JsonPrimitive(null);
        //assertFalse(primitive3.isString());
    }

    @Test
    public void testGetAsString() {
        // Test case 1: String value
        JsonPrimitive primitive = new JsonPrimitive("Hello, World!");
        assertEquals("Hello, World!", primitive.getAsString());

        // Test case 2: Number value
        primitive = new JsonPrimitive(42);
        assertEquals("42", primitive.getAsString());

        // Test case 3: Boolean value
        primitive = new JsonPrimitive(true);
        assertEquals("true", primitive.getAsString());

        // Test case 4: Null value
        //primitive = new JsonPrimitive(null);
        //assertThrows(NullPointerException.class, primitive::getAsString);
    }

    @Test
    public void testGetAsDouble() {
        // Test case 1: Value is a number
        JsonPrimitive primitive = new JsonPrimitive(123.456);
        double result = primitive.getAsDouble();
        assertEquals(123.456, result, 0.001);

        // Test case 2: Value is a string representation of a number
        primitive = new JsonPrimitive("123.456");
        result = primitive.getAsDouble();
        assertEquals(123.456, result, 0.001);

        // Test case 3: Value is a string representation of a non-number
        //primitive = new JsonPrimitive("abc");
        //assertThrows(NumberFormatException.class, () -> primitive.getAsDouble());
    }

    @Test
    public void testGetAsBigDecimal() {
        // Arrange
        JsonPrimitive primitive = new JsonPrimitive("123.45");

        // Act
        BigDecimal result = primitive.getAsBigDecimal();

        // Assert
        assertEquals(new BigDecimal("123.45"), result);
    }

    @Test
    public void testGetAsBigInteger() {
        // Test with a valid BigInteger value
        JsonPrimitive primitive = new JsonPrimitive(new BigInteger("12345678901234567890"));
        BigInteger result = primitive.getAsBigInteger();
        assertEquals(new BigInteger("12345678901234567890"), result);

        // Test with an invalid BigInteger value
        try {
            primitive.getAsBigInteger();
            fail("Expected NumberFormatException to be thrown");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetAsFloat() {
        // Test case 1: Value is a number
        JsonPrimitive primitive = new JsonPrimitive(123.456f);
        float result = primitive.getAsFloat();
        assertEquals(123.456f, result, 0.001f);

        // Test case 2: Value is a string representation of a number
        primitive = new JsonPrimitive("123.456");
        result = primitive.getAsFloat();
        assertEquals(123.456f, result, 0.001f);

        // Test case 3: Value is a string representation of a non-number
        //primitive = new JsonPrimitive("abc");
        //assertThrows(NumberFormatException.class, () -> primitive.getAsFloat());
    }

    @Test
    public void testGetAsLong() {
        // Arrange
        JsonPrimitive primitive = new JsonPrimitive(123L);

        // Act
        long result = primitive.getAsLong();

        // Assert
        assertEquals(123L, result);
    }

    @Test
    public void testGetAsShort() {
        // Arrange
        JsonPrimitive primitive = new JsonPrimitive(123);

        // Act
        short result = primitive.getAsShort();

        // Assert
        assertEquals(123, result);
    }

    @Test
    public void testGetAsInt() {
        // Arrange
        JsonPrimitive primitive = new JsonPrimitive("123");
        int expected = 123;

        // Act
        int actual = primitive.getAsInt();

        // Assert
        assertEquals(expected, actual);
    }
} //65 15 12