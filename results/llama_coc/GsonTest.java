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

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.gson.Gson; 
import com.google.gson.reflect.TypeToken; // Importa TypeToken import java.lang.reflect.Type;
import java.lang.reflect.Type; // Importa Type
import java.util.Collection;
import java.util.ArrayList; // Importa ArrayList
import java.io.StringWriter; // Importa StringWriter

import static org.junit.Assert.assertEquals;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.junit.Test;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

@SuppressWarnings("deprecation")
public class GsonTest {

    @Test
    public void testToJsonTree() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJsonTree("Hello, World!").toString();
        assertEquals("\"Hello, World!\"", json);
    }
    
    @Test
    public void testToJson() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson("hello world");
        System.out.println(json);
    }
/*
    @Test
    public void testToJson2() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(new Person("John", "Doe", 30));
        System.out.println(json);
    }

    @Test
    public void testToJson3() {
        Gson gson = new Gson();
        String json = gson.toJson(new ArrayList<String>());
        assertThat(json, notNullValue());
    }

    @Test
    public void testToJson4() throws Exception {
        Gson gson = new Gson();
        Person person = new Person("John", "Doe", 35);
        String json = gson.toJson(person);
        assertEquals("{\"name\":\"John\",\"surname\":\"Doe\",\"age\":35}", json);
    }*/

    @Test
    public void testToJson5() {
        Gson gson = new Gson();
        JsonElement element = new JsonPrimitive("hello");
        String json = gson.toJson(element);
        assertEquals("{\"value\":\"hello\"}", json);
    }

    
}