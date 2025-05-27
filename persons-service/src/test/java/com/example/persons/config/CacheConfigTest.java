package com.example.persons.config;

import com.example.persons.model.Person;
import com.example.persons.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class CacheConfigTest {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private PersonService personService;

    @Test
    void whenCacheManagerIsAutowired_thenCacheManagerIsNotNull() {
        assertNotNull(cacheManager);
        assertThat(cacheManager).isInstanceOf(RedisCacheManager.class);
    }

    @Test
    void whenPersonIsSaved_thenPersonIsCached() {
        // Create and save a person
        Person person = new Person();
        person.setName("Test Person");
        person.setEmail("test@example.com");
        person.setPhoneNumber("+1234567890");
        Person savedPerson = personService.save(person);

        // First call should hit the database
        Optional<Person> firstCall = personService.findById(savedPerson.getId());
        assertThat(firstCall).isPresent();

        // Second call should hit the cache
        Optional<Person> secondCall = personService.findById(savedPerson.getId());
        assertThat(secondCall).isPresent();

        // Verify both calls return the same object (from cache)
        assertThat(firstCall.get()).isSameAs(secondCall.get());
    }

    @Test
    void whenPersonIsUpdated_thenCacheIsEvicted() {
        // Create and save a person
        Person person = new Person();
        person.setName("Test Person");
        person.setEmail("test2@example.com");
        person.setPhoneNumber("+1234567890");
        Person savedPerson = personService.save(person);

        // First call to cache the person
        Optional<Person> firstCall = personService.findById(savedPerson.getId());
        assertThat(firstCall).isPresent();

        // Update the person
        savedPerson.setName("Updated Name");
        personService.update(savedPerson.getId(), savedPerson);

        // Call again, should get updated data
        Optional<Person> secondCall = personService.findById(savedPerson.getId());
        assertThat(secondCall).isPresent();
        assertThat(secondCall.get().getName()).isEqualTo("Updated Name");
    }

    @Test
    void whenPersonIsDeleted_thenCacheIsEvicted() {
        // Create and save a person
        Person person = new Person();
        person.setName("Test Person");
        person.setEmail("test3@example.com");
        person.setPhoneNumber("+1234567890");
        Person savedPerson = personService.save(person);

        // First call to cache the person
        Optional<Person> firstCall = personService.findById(savedPerson.getId());
        assertThat(firstCall).isPresent();

        // Delete the person
        personService.delete(savedPerson.getId());

        // Call again, should not find the person
        Optional<Person> secondCall = personService.findById(savedPerson.getId());
        assertThat(secondCall).isEmpty();
    }

    @Test
    void whenPersonByEmailIsQueried_thenPersonIsCached() {
        // Create and save a person
        Person person = new Person();
        person.setName("Test Person");
        person.setEmail("test4@example.com");
        person.setPhoneNumber("+1234567890");
        Person savedPerson = personService.save(person);

        // First call should hit the database
        Optional<Person> firstCall = personService.findByEmail(savedPerson.getEmail());
        assertThat(firstCall).isPresent();

        // Second call should hit the cache
        Optional<Person> secondCall = personService.findByEmail(savedPerson.getEmail());
        assertThat(secondCall).isPresent();

        // Verify both calls return the same object (from cache)
        assertThat(firstCall.get()).isSameAs(secondCall.get());
    }
} 