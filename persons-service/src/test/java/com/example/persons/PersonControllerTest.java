package com.example.persons;

import com.example.persons.controller.PersonController;
import com.example.persons.model.Person;
import com.example.persons.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllPersonsReturnsList() throws Exception {
        Person person = new Person();
        person.setId(1L);
        person.setName("John Doe");
        person.setEmail("john@example.com");

        when(personService.findAll()).thenReturn(List.of(person));

        mockMvc.perform(get("/api/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void getPersonByIdReturnsPerson() throws Exception {
        Person person = new Person();
        person.setId(1L);
        person.setName("John Doe");
        person.setEmail("john@example.com");

        when(personService.findById(1L)).thenReturn(Optional.of(person));

        mockMvc.perform(get("/api/persons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void getPersonByIdNotFound() throws Exception {
        when(personService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/persons/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createPersonReturnsCreated() throws Exception {
        Person person = new Person();
        person.setName("John Doe");
        person.setEmail("john@example.com");

        Person savedPerson = new Person();
        savedPerson.setId(1L);
        savedPerson.setName("John Doe");
        savedPerson.setEmail("john@example.com");

        when(personService.save(any(Person.class))).thenReturn(savedPerson);

        mockMvc.perform(post("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void createPersonsReturnsCreated() throws Exception {
        Person person = new Person();
        person.setName("John Doe");
        person.setEmail("john@example.com");

        Person savedPerson = new Person();
        savedPerson.setId(1L);
        savedPerson.setName("John Doe");
        savedPerson.setEmail("john@example.com");

        when(personService.saveAll(any(List.class))).thenReturn(List.of(savedPerson));

        mockMvc.perform(post("/api/persons/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(person))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[?(@.name == 'John Doe')]").exists());
    }

    @Test
    void updatePersonReturnsUpdated() throws Exception {
        Person person = new Person();
        person.setName("Jane Doe");
        person.setEmail("jane@example.com");

        Person updatedPerson = new Person();
        updatedPerson.setId(1L);
        updatedPerson.setName("Jane Doe");
        updatedPerson.setEmail("jane@example.com");

        when(personService.update(1L, any(Person.class))).thenReturn(Optional.of(updatedPerson));

        mockMvc.perform(put("/api/persons/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe"));
    }

    @Test
    void updatePersonNotFound() throws Exception {
        Person person = new Person();
        person.setName("Jane Doe");
        person.setEmail("jane@example.com");

        when(personService.update(1L, any(Person.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/persons/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePersonReturnsNoContent() throws Exception {
        when(personService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/persons/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePersonNotFound() throws Exception {
        when(personService.delete(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/persons/1"))
                .andExpect(status().isNotFound());
    }
}