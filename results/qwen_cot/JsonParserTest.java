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
import static org.junit.Assert.*;

public class JsonParserTest {

    @Test @SuppressWarnings("deprecation")
    public void testParseString() {
        // Arrange
        String json = "{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}";

        // Act
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);

        // Assert
        assertEquals("John", element.getAsJsonObject().get("name").getAsString());
        assertEquals(30, element.getAsJsonObject().get("age").getAsInt());
        assertEquals("New York", element.getAsJsonObject().get("city").getAsString());
    }
    
   /* @Test
    public void testParseReader() {
        // Arrange
        JsonReader reader = new JsonReader(new StringReader("{ \"name\": \"John\", \"age\": 30 }"));
        JsonParser parser = new JsonParser();

        // Act
        JsonElement element = parser.parseReader(reader);

        // Assert
        assertEquals("John", element.getAsJsonObject().get("name").getAsString());
        assertEquals(30, element.getAsJsonObject().get("age").getAsInt());
    }*/
    
    //tercer test mal generado
    
}
