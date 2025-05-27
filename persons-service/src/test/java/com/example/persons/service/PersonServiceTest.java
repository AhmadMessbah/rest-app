package com.example.persons.service;

import com.example.persons.config.TestConfig;
import com.example.persons.model.Person;
import com.example.persons.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles("test")
@Transactional
class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    private Person testPerson;

    @BeforeEach
    void setUp() {
        personRepository.deleteAll();
        
        testPerson = new Person();
        testPerson.setName("John Doe");
        testPerson.setEmail("john.doe@example.com");
        testPerson.setPhoneNumber("+1234567890");
        testPerson.setAddress("123 Test St");
        testPerson.setAge(30);
    }

    @Test
    void whenSavePerson_thenPersonIsSaved() {
        // When
        Person savedPerson = personService.save(testPerson);

        // Then
        assertThat(savedPerson).isNotNull();
        assertThat(savedPerson.getId()).isNotNull();
        assertThat(savedPerson.getName()).isEqualTo(testPerson.getName());
        assertThat(savedPerson.getEmail()).isEqualTo(testPerson.getEmail());
    }

    @Test
    void whenFindById_thenPersonIsFound() {
        // Given
        Person savedPerson = personService.save(testPerson);

        // When
        Optional<Person> foundPerson = personService.findById(savedPerson.getId());

        // Then
        assertThat(foundPerson).isPresent();
        assertThat(foundPerson.get().getName()).isEqualTo(testPerson.getName());
    }

    @Test
    void whenFindByEmail_thenPersonIsFound() {
        // Given
        Person savedPerson = personService.save(testPerson);

        // When
        Optional<Person> foundPerson = personService.findByEmail(testPerson.getEmail());

        // Then
        assertThat(foundPerson).isPresent();
        assertThat(foundPerson.get().getId()).isEqualTo(savedPerson.getId());
    }

    @Test
    void whenFindAll_thenAllPersonsAreReturned() {
        // Given
        Person person2 = new Person();
        person2.setName("Jane Doe");
        person2.setEmail("jane.doe@example.com");
        person2.setPhoneNumber("+0987654321");
        person2.setAddress("456 Test Ave");
        person2.setAge(28);

        personService.saveAll(Arrays.asList(testPerson, person2));

        // When
        Page<Person> persons = personService.findAll(PageRequest.of(0, 10));

        // Then
        assertThat(persons.getContent()).hasSize(2);
        assertThat(persons.getContent()).extracting(Person::getEmail)
                .containsExactlyInAnyOrder(testPerson.getEmail(), person2.getEmail());
    }

    @Test
    void whenUpdatePerson_thenPersonIsUpdated() {
        // Given
        Person savedPerson = personService.save(testPerson);
        savedPerson.setName("Updated Name");
        savedPerson.setPhoneNumber("+9876543210");

        // When
        Optional<Person> updatedPerson = personService.update(savedPerson.getId(), savedPerson);

        // Then
        assertThat(updatedPerson).isPresent();
        assertThat(updatedPerson.get().getName()).isEqualTo("Updated Name");
        assertThat(updatedPerson.get().getPhoneNumber()).isEqualTo("+9876543210");
        assertThat(updatedPerson.get().getCreatedAt()).isEqualTo(savedPerson.getCreatedAt());
    }

    @Test
    void whenDeletePerson_thenPersonIsSoftDeleted() {
        // Given
        Person savedPerson = personService.save(testPerson);

        // When
        boolean deleted = personService.delete(savedPerson.getId());

        // Then
        assertThat(deleted).isTrue();
        Optional<Person> foundPerson = personService.findById(savedPerson.getId());
        assertThat(foundPerson).isEmpty();
    }

    @Test
    void whenHardDeletePerson_thenPersonIsRemoved() {
        // Given
        Person savedPerson = personService.save(testPerson);

        // When
        boolean deleted = personService.hardDelete(savedPerson.getId());

        // Then
        assertThat(deleted).isTrue();
        Optional<Person> foundPerson = personRepository.findById(savedPerson.getId());
        assertThat(foundPerson).isEmpty();
    }

    @Test
    void whenSearchPersons_thenMatchingPersonsAreReturned() {
        // Given
        Person person2 = new Person();
        person2.setName("Jane Smith");
        person2.setEmail("jane.smith@example.com");
        person2.setPhoneNumber("+0987654321");
        person2.setAddress("456 Test Ave");
        person2.setAge(28);

        personService.saveAll(Arrays.asList(testPerson, person2));

        // When
        Page<Person> searchResults = personService.search("john", PageRequest.of(0, 10));

        // Then
        assertThat(searchResults.getContent()).hasSize(1);
        assertThat(searchResults.getContent().get(0).getEmail()).isEqualTo(testPerson.getEmail());
    }

    @Test
    void whenSavePersonWithDuplicateEmail_thenExceptionIsThrown() {
        // Given
        personService.save(testPerson);

        Person duplicatePerson = new Person();
        duplicatePerson.setName("Another Person");
        duplicatePerson.setEmail(testPerson.getEmail());
        duplicatePerson.setPhoneNumber("+1111111111");

        // When/Then
        assertThrows(Exception.class, () -> personService.save(duplicatePerson));
    }
} 