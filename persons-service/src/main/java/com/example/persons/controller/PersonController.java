package com.example.persons.controller;

import com.example.persons.model.Person;
import com.example.persons.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Person entities.
 */
@RestController
@RequestMapping("/api/persons")
@Tag(name = "Person Management", description = "APIs for managing persons")
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
    @Operation(summary = "Get all persons", description = "Retrieves a paginated list of all persons in the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of persons"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<Person>> getAllPersons(
            @Parameter(description = "Pagination and sorting parameters") 
            @PageableDefault(size = 20) Pageable pageable) {
        logger.info("Request to retrieve all persons with pagination: {}", pageable);
        return ResponseEntity.ok(personService.findAll(pageable));
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
    public ResponseEntity<Person> getPersonById(
            @Parameter(description = "Person ID") @PathVariable Long id) {
        logger.info("Request to retrieve person with ID: {}", id);
        return personService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Person with ID {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Retrieves a person by email.
     * @param email The email of the person.
     * @return The person if found, or 404 if not found.
     */
    @Operation(summary = "Get a person by email", description = "Retrieves a person by their email address.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved person"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<Person> getPersonByEmail(
            @Parameter(description = "Person email") @PathVariable String email) {
        logger.info("Request to retrieve person with email: {}", email);
        return personService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Person with email {} not found", email);
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
    public ResponseEntity<Person> createPerson(
            @Parameter(description = "Person data") @Valid @RequestBody Person person) {
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
    public ResponseEntity<List<Person>> createPersons(
            @Parameter(description = "List of persons") @Valid @RequestBody List<Person> persons) {
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
    public ResponseEntity<Person> updatePerson(
            @Parameter(description = "Person ID") @PathVariable Long id,
            @Parameter(description = "Updated person data") @Valid @RequestBody Person person) {
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
    @Operation(summary = "Soft delete a person", description = "Marks a person as deleted without removing from database.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Person deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(
            @Parameter(description = "Person ID") @PathVariable Long id) {
        logger.info("Request to soft delete person with ID: {}", id);
        boolean deleted = personService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Person with ID {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Hard deletes a person by ID.
     * @param id The ID of the person to delete.
     * @return 204 if deleted, or 404 if not found.
     */
    @Operation(summary = "Hard delete a person", description = "Permanently removes a person from the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Person deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeletePerson(
            @Parameter(description = "Person ID") @PathVariable Long id) {
        logger.info("Request to hard delete person with ID: {}", id);
        boolean deleted = personService.hardDelete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Person with ID {} not found for hard deletion", id);
            return ResponseEntity.notFound().build();
        }
    }
}