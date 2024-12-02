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
import static org.junit.Assert.*; 
import org.junit.Test;
import com.google.gson.FieldAttributes; 
import java.lang.reflect.Field;

//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.assertEquals;

public class FieldAttributesTest {

    /*@Test
    public void testGetDeclaringClass() {
        // Define el resultado esperado para diferentes entradas del método.
        FieldAttributes fieldAttributes = new FieldAttributes();
        Class<?> declaringClass = fieldAttributes.getDeclaringClass();

        // Asegúrate de incluir tanto los casos típicos como los casos límite.
        assertEquals(declaringClass, fieldAttributes.getDeclaringClass());
    }
    
    @Test
    public void testGetNameWithValidInput() {
        FieldAttributes fieldAttributes = new FieldAttributes();
        String expectedName = "field1";
        fieldAttributes.setName(expectedName);
        assertEquals(expectedName, fieldAttributes.getName());
    }

    @Test
    public void testGetNameWithInvalidInput() {
        FieldAttributes fieldAttributes = new FieldAttributes();
        String expectedName = "field1";
        fieldAttributes.setName(expectedName);
        assertEquals(expectedName, fieldAttributes.getName());
    }

    @Test
    public void testGetNameWithNullInput() {
        FieldAttributes fieldAttributes = new FieldAttributes();
        String expectedName = "field1";
        fieldAttributes.setName(expectedName);
        assertEquals(expectedName, fieldAttributes.getName());
    }   

    @Test
    public void testGetAnnotation_ReturnsNull_WhenNoAnnotation() {
        // Arrange
        FieldAttributes fieldAttributes = new FieldAttributes();
        Class<?> annotationClass = SomeAnnotation.class;

        // Act
        SomeAnnotation annotation = fieldAttributes.getAnnotation(annotationClass);

        // Assert
        assertEquals(null, annotation);
    }

    @Test
    public void testGetAnnotations() {
        // Define el resultado esperado para diferentes entradas del método.
        // Asegúrate de incluir tanto los casos típicos como los casos límite.
        FieldAttributes fieldAttributes = new FieldAttributes();
        fieldAttributes.setAnnotations(new Annotation[]{});

        // Caso típico: entrada válida
        Annotation[] expectedAnnotations = new Annotation[]{};
        assertEquals(expectedAnnotations, fieldAttributes.getAnnotations());

        // Caso límite: entrada con un solo anotación
        Annotation[] expectedAnnotationsWithOne = new Annotation[]{new Annotation()};
        fieldAttributes.setAnnotations(expectedAnnotationsWithOne);
        assertEquals(expectedAnnotationsWithOne, fieldAttributes.getAnnotations());    
    }*/
}