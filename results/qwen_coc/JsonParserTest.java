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
import com.google.gson.JsonParser; 
import com.google.gson.stream.JsonReader; 
import java.io.StringReader;

@SuppressWarnings({"deprecation", "unchecked", "static-access"})
public class JsonParserTest {

    @Test
    public void testParseString() {
        // Test case 1: Valid JSON string
        String json = "{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}";
        JsonElement element = JsonParser.parseString(json);
        assertNotNull(element);
        assertEquals("John", element.getAsJsonObject().get("name").getAsString());
        assertEquals(30, element.getAsJsonObject().get("age").getAsInt());
        assertEquals("New York", element.getAsJsonObject().get("city").getAsString());
    }
    
    @SuppressWarnings("static-access")
    @Test
    public void testParseReader() {
        // Arrange
        JsonReader jsonReader = new JsonReader(new StringReader("{ \"name\": \"John\", \"age\": 30 }"));
        JsonParser jsonParser = new JsonParser();

        // Act
        JsonElement jsonElement = jsonParser.parseReader(jsonReader);

        // Assert
        assertEquals("John", jsonElement.getAsJsonObject().get("name").getAsString());
        assertEquals(30, jsonElement.getAsJsonObject().get("age").getAsInt());
    }
    
    @SuppressWarnings("static-access")
    @Test
    public void testParseReader2() {
        // Arrange
        JsonParser parser = new JsonParser();
        JsonReader reader = new JsonReader(new StringReader("{ \"name\": \"John\", \"age\": 30 }"));

        // Act
        JsonElement element = parser.parseReader(reader);

        // Assert
        assertEquals("John", element.getAsJsonObject().get("name").getAsString());
        assertEquals(30, element.getAsJsonObject().get("age").getAsInt());
    }
    

}
