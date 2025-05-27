package com.example.persons.controller;

import com.example.persons.model.Person;
import com.example.persons.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Person entities.
 */
@RestController
@RequestMapping("/api/persons")
public class PersonController {
    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Retrieves all persons.
     * @return List of all persons.
     */
    @Operation(summary = "Get all persons", description = "Retrieves a list of all persons in the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of persons"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        logger.info("Request to retrieve all persons");
        List<Person> persons = personService.findAll();
        return ResponseEntity.ok(persons);
    }

    /**
     * Retrieves a person by ID.
     * @param id The ID of the person.
     * @return The person if found, or 404 if not found.
     */
    @Operation(summary = "Get a person by ID", description = "Retrieves a person by their unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved person"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        logger.info("Request to retrieve person with ID: {}", id);
        return personService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Person with ID {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Creates a new person.
     * @param person The person to create.
     * @return The created person.
     */
    @Operation(summary = "Create a person", description = "Creates a new person in the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Person created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Person> createPerson(@Valid @RequestBody Person person) {
        logger.info("Request to create person: {}", person.getName());
        Person savedPerson = personService.save(person);
        return ResponseEntity.status(201).body(savedPerson);
    }

    /**
     * Creates multiple persons.
     * @param persons The list of persons to create.
     * @return The created persons.
     */
    @Operation(summary = "Create multiple persons", description = "Creates multiple persons in the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Persons created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/batch")
    public ResponseEntity<List<Person>> createPersons(@Valid @RequestBody List<Person> persons) {
        logger.info("Request to create {} persons", persons.size());
        List<Person> savedPersons = personService.saveAll(persons);
        return ResponseEntity.status(201).body(savedPersons);
    }

    /**
     * Updates an existing person.
     * @param id The ID of the person to update.
     * @param person The updated person data.
     * @return The updated person if found, or 404 if not found.
     */
    @Operation(summary = "Update a person", description = "Updates an existing person by their ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Person updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @Valid @RequestBody Person person) {
        logger.info("Request to update person with ID: {}", id);
        return personService.update(id, person)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Person with ID {} not found for update", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Deletes a person by ID.
     * @param id The ID of the person to delete.
     * @return 204 if deleted, or 404 if not found.
     */
    @Operation(summary = "Delete a person", description = "Deletes a person by their ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Person deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        logger.info("Request to delete person with ID: {}", id);
        boolean deleted = personService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Person with ID {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
    }
}