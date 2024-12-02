/*
 * Copyright (C) 2016 The Gson Authors
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;
import com.google.gson.reflect.TypeToken;
import java.util.Map; // Importar la clase Map
import java.io.StringReader; // Importar la clase StringReader

//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings({"deprecation", "unchecked"})
public class GsonTest {

    /*@Test
    public void testToJson() {
        // Option 1: Verify expected return value
        Gson gson = new Gson();
        String json = gson.toJson(new Person("John Doe", 30));
        assertEquals("{\"name\":\"John Doe\",\"age\":30}", json);

        // Option 2: Evaluate behavior for valid and invalid inputs
        json = gson.toJson(new Person("Jane Doe", 31));
        assertEquals("{\"name\":\"Jane Doe\",\"age\":31}", json);
        json = gson.toJson(new Person("John Doe", 30, "Male"));
        assertEquals("{\"name\":\"John Doe\",\"age\":30,\"gender\":\"Male\"}", json);
        json = gson.toJson(new Person("Jane Doe", 31, "Female"));
        assertEquals("{\"name\":\"Jane Doe\",\"age\":31,\"gender\":\"Female\"}", json);
        json = gson.toJson(new Person("John Doe", 30, null));
        assertEquals("{\"name\":\"John Doe\",\"age\":30,\"gender\":null}", json);

        // Option 3: Evaluate side effects
        Person person = new Person("John Doe", 30);
        gson.toJson(person);
        assertEquals("{\"name\":\"John Doe\",\"age\":30}", gson.toJson(person));
    }*/
    
    @Test
    public void testFromJson() {
        // Option 1: Verify the expected return value of the method
        // Define the expected result for different input values of the method
        // Example:
        Gson gson = new Gson();
        String json = "[\"hello\", \"world\"]";
        Object result = gson.fromJson(json, String[].class);
        assertEquals("hello world", result);

        // Option 2: Evaluate the behavior of the method against different inputs
        // Consider valid and invalid input values that the method might receive
        // Example:
        //Gson gson = new Gson();
        //String json = "[]";
        //Object result = gson.fromJson(json, String[].class);
        //assertNull(result);

        // Option 3: Evaluate the side effects of the method
        // If the method modifies any state or global variable, verify that the changes are expected
        // If the method does not modify any state or global variable, verify that there are no unintended side effects
        // Example:
        //Gson gson = new Gson();
        //String json = "[\"hello\", \"world\"]";
        //Object result = gson.fromJson(json, String[].class);
        //String expected = "hello world";
    }
    
}