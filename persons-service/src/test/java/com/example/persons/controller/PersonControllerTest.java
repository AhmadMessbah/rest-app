package com.example.persons.controller;

import com.example.persons.config.TestConfig;
import com.example.persons.model.Person;
import com.example.persons.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
@ActiveProfiles("test")
@Transactional
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    private Person testPerson;

    @BeforeEach
    void setUp() {
        testPerson = new Person();
        testPerson.setName("John Doe");
        testPerson.setEmail("john.doe@example.com");
        testPerson.setPhoneNumber("+1234567890");
        testPerson.setAddress("123 Test St");
        testPerson.setAge(30);
    }

    @Test
    @WithMockUser
    void whenGetAllPersons_thenReturnPersonsList() throws Exception {
        // Given
        Person savedPerson = personService.save(testPerson);

        // When/Then
        mockMvc.perform(get("/api/persons")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].email", is(testPerson.getEmail())))
                .andExpect(jsonPath("$.content[0].name", is(testPerson.getName())));
    }

    @Test
    @WithMockUser
    void whenGetPersonById_thenReturnPerson() throws Exception {
        // Given
        Person savedPerson = personService.save(testPerson);

        // When/Then
        mockMvc.perform(get("/api/persons/{id}", savedPerson.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedPerson.getId().intValue())))
                .andExpect(jsonPath("$.email", is(testPerson.getEmail())))
                .andExpect(jsonPath("$.name", is(testPerson.getName())));
    }

    @Test
    @WithMockUser
    void whenGetPersonByEmail_thenReturnPerson() throws Exception {
        // Given
        Person savedPerson = personService.save(testPerson);

        // When/Then
        mockMvc.perform(get("/api/persons/email/{email}", testPerson.getEmail())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedPerson.getId().intValue())))
                .andExpect(jsonPath("$.email", is(testPerson.getEmail())));
    }

    @Test
    @WithMockUser
    void whenCreatePerson_thenReturnCreatedPerson() throws Exception {
        // When/Then
        mockMvc.perform(post("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPerson)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.email", is(testPerson.getEmail())))
                .andExpect(jsonPath("$.name", is(testPerson.getName())));
    }

    @Test
    @WithMockUser
    void whenCreateMultiplePersons_thenReturnCreatedPersons() throws Exception {
        // Given
        Person person2 = new Person();
        person2.setName("Jane Doe");
        person2.setEmail("jane.doe@example.com");
        person2.setPhoneNumber("+0987654321");
        person2.setAddress("456 Test Ave");
        person2.setAge(28);

        List<Person> persons = Arrays.asList(testPerson, person2);

        // When/Then
        mockMvc.perform(post("/api/persons/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(persons)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email", is(testPerson.getEmail())))
                .andExpect(jsonPath("$[1].email", is(person2.getEmail())));
    }

    @Test
    @WithMockUser
    void whenUpdatePerson_thenReturnUpdatedPerson() throws Exception {
        // Given
        Person savedPerson = personService.save(testPerson);
        savedPerson.setName("Updated Name");
        savedPerson.setPhoneNumber("+9876543210");

        // When/Then
        mockMvc.perform(put("/api/persons/{id}", savedPerson.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(savedPerson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedPerson.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Updated Name")))
                .andExpect(jsonPath("$.phoneNumber", is("+9876543210")));
    }

    @Test
    @WithMockUser
    void whenDeletePerson_thenReturnNoContent() throws Exception {
        // Given
        Person savedPerson = personService.save(testPerson);

        // When/Then
        mockMvc.perform(delete("/api/persons/{id}", savedPerson.getId()))
                .andExpect(status().isNoContent());

        // Verify person is soft deleted
        mockMvc.perform(get("/api/persons/{id}", savedPerson.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void whenHardDeletePerson_thenReturnNoContent() throws Exception {
        // Given
        Person savedPerson = personService.save(testPerson);

        // When/Then
        mockMvc.perform(delete("/api/persons/{id}/hard", savedPerson.getId()))
                .andExpect(status().isNoContent());

        // Verify person is hard deleted
        mockMvc.perform(get("/api/persons/{id}", savedPerson.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void whenCreatePersonWithInvalidData_thenReturnBadRequest() throws Exception {
        // Given
        testPerson.setEmail("invalid-email");

        // When/Then
        mockMvc.perform(post("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPerson)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", notNullValue()));
    }

    @Test
    void whenAccessProtectedEndpointWithoutAuth_thenReturnUnauthorized() throws Exception {
        // When/Then
        mockMvc.perform(get("/api/persons"))
                .andExpect(status().isUnauthorized());
    }
} 