/*
 * Copyright (C) 2009 Google Inc.
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.io.StringReader;
import static org.junit.Assert.assertTrue;
import com.google.gson.JsonStreamParser; 
import java.io.StringReader;

public class JsonStreamParserTest {

   /* @Test
    public void testNext() {
        // Arrange
        JsonStreamParser parser = new JsonStreamParser();
        String json = "{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}";

        // Act
        JsonElement element = parser.next(json);

        // Assert
        assertNotNull(element);
        assertEquals("John", element.getAsJsonObject().get("name").getAsString());
        assertEquals(30, element.getAsJsonObject().get("age").getAsInt());
        assertEquals("New York", element.getAsJsonObject().get("city").getAsString());
    }*/
    
    @Test
    public void testHasNext() {
        // Arrange
        JsonStreamParser parser = new JsonStreamParser(new StringReader("{ \"name\": \"John\", \"age\": 30 }"));
        JsonElement element = parser.next();

        // Act
        boolean hasNext = parser.hasNext();

        // Assert
        assertTrue(hasNext);
    }

    /*@Test
    public void testRemove() {
        // Arrange
        JsonStreamParser parser = new JsonStreamParser();
        // Act
        parser.remove();
        // Assert
        assertEquals(0, parser.size());
    }    */
}