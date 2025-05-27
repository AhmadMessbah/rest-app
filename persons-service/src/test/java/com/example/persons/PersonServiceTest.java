package com.example.persons;

import com.example.persons.model.Person;
import com.example.persons.repository.PersonRepository;
import com.example.persons.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @MockBean
    private PersonRepository personRepository;

    @Test
    void testGetPersonById() {
        Person person = new Person();
        person.setId(1L);
        person.setName("John Doe");
        person.setEmail("john@example.com");

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        Optional<Person> result = personService.getPersonById(1L);
        assertTrue(result.isPresent());
        assertTrue(result.get().getName().equals("John Doe"));
    }
}