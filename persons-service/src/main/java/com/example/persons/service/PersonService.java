package com.example.persons.service;

import com.example.persons.model.Person;
import com.example.persons.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * Retrieves all persons from the database with pagination.
     * @param pageable The pagination information.
     * @return Page of persons.
     */
    @Cacheable("persons")
    @Transactional(readOnly = true)
    public Page<Person> findAll(Pageable pageable) {
        logger.info("Fetching persons with pagination: {}", pageable);
        return personRepository.findAll(pageable);
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
     * Retrieves a person by their email.
     * @param email The email of the person.
     * @return Optional containing the person, or empty if not found.
     */
    @Cacheable(value = "personByEmail", key = "#email")
    @Transactional(readOnly = true)
    public Optional<Person> findByEmail(String email) {
        logger.info("Fetching person with email: {}", email);
        return personRepository.findByEmail(email);
    }

    /**
     * Saves a single person to the database.
     * @param person The person to save.
     * @return The saved person.
     */
    @CacheEvict(value = {"persons", "person", "personByEmail"}, allEntries = true)
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
    @CacheEvict(value = {"persons", "person", "personByEmail"}, allEntries = true)
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
    @CacheEvict(value = {"persons", "person", "personByEmail"}, allEntries = true)
    @Transactional
    public Optional<Person> update(Long id, Person person) {
        logger.info("Updating person with ID: {}", id);
        return personRepository.findById(id)
                .map(existing -> {
                    person.setId(id);
                    person.setCreatedAt(existing.getCreatedAt()); // Preserve creation time
                    return personRepository.save(person);
                });
    }

    /**
     * Deletes a person by their ID.
     * @param id The ID of the person to delete.
     * @return true if the person was deleted, false if not found.
     */
    @CacheEvict(value = {"persons", "person", "personByEmail"}, allEntries = true)
    @Transactional
    public boolean delete(Long id) {
        logger.info("Soft deleting person with ID: {}", id);
        return personRepository.findById(id)
                .map(person -> {
                    person.setDeleted(true);
                    personRepository.save(person);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Hard deletes a person by their ID.
     * @param id The ID of the person to delete.
     * @return true if the person was deleted, false if not found.
     */
    @CacheEvict(value = {"persons", "person", "personByEmail"}, allEntries = true)
    @Transactional
    public boolean hardDelete(Long id) {
        logger.info("Hard deleting person with ID: {}", id);
        return personRepository.findById(id)
                .map(person -> {
                    personRepository.delete(person);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Searches for persons based on a query and pagination.
     * @param query The search query.
     * @param pageable The pagination information.
     * @return Page of persons matching the query.
     */
    @Transactional(readOnly = true)
    public Page<Person> search(String query, Pageable pageable) {
        logger.info("Searching persons with query: {}", query);
        return personRepository.findByNameContainingOrEmailContaining(query, query, pageable);
    }
}