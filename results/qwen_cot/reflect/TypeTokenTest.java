/*
 * Copyright (C) 2010 Google Inc.
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

package com.google.gson.reflect;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
@SuppressWarnings("deprecation")

public class TypeTokenTest {
    @Test
    public void testGetRawType() {
        TypeToken<String> typeToken = new TypeToken<String>() {};
        assertEquals(String.class, typeToken.getRawType());
    }
    
    @Test
    public void getType() {
        TypeToken<String> typeToken = new TypeToken<String>() {};
        assertEquals(String.class, typeToken.getType());
    }
    
    @Test
    public void testIsAssignableFrom() {
        TypeToken<String> typeToken = new TypeToken<String>() {};
        assertTrue(typeToken.isAssignableFrom(String.class));
        assertFalse(typeToken.isAssignableFrom(Integer.class));
    }

    @Test
    public void testIsAssignableFrom2() {
        TypeToken<String> typeToken = new TypeToken<String>() {};
        assertTrue(typeToken.isAssignableFrom(String.class));
        assertFalse(typeToken.isAssignableFrom(Integer.class));
    }

    @Test
    public void testIsAssignableFrom3() {
        TypeToken<String> typeToken = new TypeToken<String>() {};
        assertTrue(typeToken.isAssignableFrom(String.class));
        assertFalse(typeToken.isAssignableFrom(Integer.class));
    }

    @Test
    public void testIsAssignableFrom4() {
        TypeToken<String> typeToken1 = new TypeToken<String>() {};
        TypeToken<String> typeToken2 = new TypeToken<String>() {};
        TypeToken<Integer> typeToken3 = new TypeToken<Integer>() {};

        assertTrue(typeToken1.isAssignableFrom(typeToken2));
        assertFalse(typeToken1.isAssignableFrom(typeToken3));
    }

    @Test
    public void testHashCode() {
        TypeToken<String> typeToken = new TypeToken<String>() {};
        int expectedHashCode = typeToken.hashCode();
        assertEquals(expectedHashCode, typeToken.hashCode());
    }  

    @Test
    public void testEquals() {
        TypeToken<String> typeToken1 = new TypeToken<String>() {};
        TypeToken<String> typeToken2 = new TypeToken<String>() {};
        assertTrue(typeToken1.equals(typeToken2));
    }

    @Test
    public void testToString() {
        TypeToken<String> typeToken = new TypeToken<String>() {};
        assertEquals("String", typeToken.toString());
    }    
    
    @Test
    public void testToString2() {
        TypeToken<String> typeToken = new TypeToken<String>() {};
        assertEquals("String", typeToken.toString());
    }    
    
    @Test
    public void testGet() {
        TypeToken<String> typeToken = new TypeToken<String>() {};
        assertEquals(String.class, typeToken.getType());
    }

   /* @Test
    public void testGet2() {
        // Arrange
        TypeToken<String> typeToken = new TypeToken<String>() {};

        // Act
        String expectedResult = "Hello, World!";
        String actualResult = typeToken.getType();

        // Assert
        assertEquals(expectedResult, actualResult);
        assertTrue(actualResult!= null);
        assertFalse(actualResult.isEmpty());
    }    */
}