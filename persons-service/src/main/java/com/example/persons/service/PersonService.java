package com.example.persons.service;

import com.example.persons.model.Person;
import com.example.persons.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Person entities with CRUD operations.
 */
@Service
public class PersonService {
    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Retrieves all persons from the database.
     * @return List of all persons.
     */
    @Cacheable("persons")
    @Transactional(readOnly = true)
    public List<Person> findAll() {
        logger.info("Fetching all persons");
        return personRepository.findAll();
    }

    /**
     * Retrieves a person by their ID.
     * @param id The ID of the person.
     * @return Optional containing the person, or empty if not found.
     */
    @Cacheable(value = "person", key = "#id")
    @Transactional(readOnly = true)
    public Optional<Person> findById(Long id) {
        logger.info("Fetching person with ID: {}", id);
        return personRepository.findById(id);
    }

    /**
     * Saves a single person to the database.
     * @param person The person to save.
     * @return The saved person.
     */
    @CacheEvict(value = "persons", allEntries = true)
    @Transactional
    public Person save(Person person) {
        logger.info("Saving person: {}", person.getName());
        return personRepository.save(person);
    }

    /**
     * Saves a list of persons to the database.
     * @param persons The list of persons to save.
     * @return The saved persons.
     */
    @CacheEvict(value = "persons", allEntries = true)
    @Transactional
    public List<Person> saveAll(List<Person> persons) {
        logger.info("Saving {} persons", persons.size());
        return personRepository.saveAll(persons);
    }

    /**
     * Updates an existing person.
     * @param id The ID of the person to update.
     * @param person The updated person data.
     * @return Optional containing the updated person, or empty if not found.
     */
    @CacheEvict(value = {"persons", "person"}, allEntries = true)
    @Transactional
    public Optional<Person> update(Long id, Person person) {
        logger.info("Updating person with ID: {}", id);
        return personRepository.findById(id)
                .map(existing -> {
                    person.setId(id); // Ensure ID is not changed
                    return personRepository.save(person);
                });
    }

    /**
     * Deletes a person by their ID.
     * @param id The ID of the person to delete.
     * @return true if the person was deleted, false if not found.
     */
    @CacheEvict(value = {"persons", "person"}, allEntries = true)
    @Transactional
    public boolean delete(Long id) {
        logger.info("Deleting person with ID: {}", id);
        return personRepository.findById(id)
                .map(person -> {
                    personRepository.delete(person);
                    return true;
                })
                .orElse(false);
    }
}