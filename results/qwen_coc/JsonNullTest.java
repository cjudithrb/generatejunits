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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import com.google.gson.JsonNull; 
//import org.junit.jupiter.api.Test; 
//import static org.junit.jupiter.api.Assertions.assertFalse; 
//import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonNullTest {

    @Test
    public void testHashCode() {
        JsonNull null1 = JsonNull.INSTANCE;
        JsonNull null2 = JsonNull.INSTANCE;

        assertEquals(null1.hashCode(), null2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonNull null1 = JsonNull.INSTANCE;
        JsonNull null2 = JsonNull.INSTANCE;

        assertTrue(null1.equals(null2));
    }
}