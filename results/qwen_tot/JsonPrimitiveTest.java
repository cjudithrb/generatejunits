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

    /*@Test
    public void testIsBoolean() {
        // Casos típicos
        JsonPrimitive booleanPrimitive = new JsonPrimitive(true);
        JsonPrimitive stringPrimitive = new JsonPrimitive("true");
        //JsonPrimitive nullPrimitive = new JsonPrimitive(null);
        JsonPrimitive emptyPrimitive = new JsonPrimitive("");

        // Casos límite
        JsonPrimitive emptyStringPrimitive = new JsonPrimitive("");
        //JsonPrimitive emptyArrayPrimitive = new JsonPrimitive(new JsonArray());
        JsonPrimitive emptyObjectPrimitive = new JsonPrimitive(new JsonObject());

        // Casos de entrada inválida
        JsonPrimitive invalidPrimitive = new JsonPrimitive(new JsonNumber(1));
    }*/
    
    @Test
    public void testIsNumberWithNumber() {
        JsonPrimitive primitive = new JsonPrimitive(123);
        boolean result = primitive.isNumber();
        assertEquals(true, result);
    }

    @Test
    public void testIsNumberWithString() {
        JsonPrimitive primitive = new JsonPrimitive("123");
        boolean result = primitive.isNumber();
        assertEquals(false, result);
    }

    /*@Test
    public void testIsNumberWithNull() {
        JsonPrimitive primitive = new JsonPrimitive(null);
        boolean result = primitive.isNumber();
        assertEquals(false, result);
    }*/

    @Test
    public void testIsNumberWithBoolean() {
        JsonPrimitive primitive = new JsonPrimitive(true);
        boolean result = primitive.isNumber();
        assertEquals(false, result);
    }
    
    @Test
    public void testGetAsNumber() {
        // Opción 1: Verificar el valor de retorno esperado del método
        assertEquals(1, new JsonPrimitive("1").getAsNumber().intValue());
        assertEquals(1.5, new JsonPrimitive("1.5").getAsNumber().doubleValue());
        assertEquals(1, new JsonPrimitive("1").getAsNumber().intValue());
        assertEquals(1.5, new JsonPrimitive("1.5").getAsNumber().doubleValue());
        assertEquals(1, new JsonPrimitive("1").getAsNumber().intValue());
    }
    
    @Test
    public void testIsStringWithStringValue() {
        JsonPrimitive primitive = new JsonPrimitive("Hello, World!");
        boolean result = primitive.isString();
        assertEquals(true, result);
    }

    /*@Test
    public void testIsStringWithNullValue() {
        JsonPrimitive primitive = new JsonPrimitive(null);
        boolean result = primitive.isString();
        assertEquals(false, result);
    }*/

    @Test
    public void testIsStringWithEmptyString() {
        JsonPrimitive primitive = new JsonPrimitive("");
        boolean result = primitive.isString();
        assertEquals(true, result);
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
    
    @Test
    public void testGetAsDouble() {
        // Casos típicos
        JsonPrimitive primitive = new JsonPrimitive(123.45);
        assertEquals(123.45, primitive.getAsDouble());
    }
    
    @Test
    public void testGetAsBigDecimal() {
        // Define el resultado esperado para diferentes entradas del método.
        // Asegúrate de incluir tanto los casos típicos como los casos límite.
        assertEquals(new BigDecimal("123.45"), new JsonPrimitive("123.45").getAsBigDecimal());
        assertEquals(new BigDecimal("0.00"), new JsonPrimitive("0.00").getAsBigDecimal());
        assertEquals(new BigDecimal("-123.45"), new JsonPrimitive("-123.45").getAsBigDecimal());
    }
    
    @Test
    public void testGetAsBigInteger() {
        // Casos típicos
        JsonPrimitive primitive = new JsonPrimitive("123");
        BigInteger expected = new BigInteger("123");
        assertEquals(expected, primitive.getAsBigInteger());

        primitive = new JsonPrimitive("12345678901234567890");
        expected = new BigInteger("12345678901234567890");
        assertEquals(expected, primitive.getAsBigInteger());
    }
    
    @Test
    public void testGetAsFloat() {
        // Caso típico: Entrada válida
        JsonPrimitive primitive = new JsonPrimitive(123.45f);
        float result = primitive.getAsFloat();
        assertEquals(123.45f, result);
    }
    
    @Test
    public void testGetAsShort() {
        // Define el resultado esperado para diferentes entradas del método
        assertEquals(1, new JsonPrimitive("1").getAsShort());
        assertEquals(10, new JsonPrimitive("10").getAsShort());
        assertEquals(100, new JsonPrimitive("100").getAsShort());
        assertEquals(1000, new JsonPrimitive("1000").getAsShort());
        assertEquals(10000, new JsonPrimitive("10000").getAsShort());
        assertEquals(100000, new JsonPrimitive("100000").getAsShort());
    }
    
    @Test
    public void testGetAsInt() {
        // Test case 1: Valid number
        JsonPrimitive primitive = new JsonPrimitive(123);
        assertEquals(123, primitive.getAsInt());
    }
}