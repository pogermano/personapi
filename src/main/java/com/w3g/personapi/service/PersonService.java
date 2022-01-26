package com.w3g.personapi.service;

import com.w3g.personapi.dto.request.PersonDTO;
import com.w3g.personapi.dto.response.MessageResponseDTO;
import com.w3g.personapi.entity.Person;
import com.w3g.personapi.mapper.PersonMapper;
import com.w3g.personapi.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonMapper personMapper = PersonMapper.INSTANCE;

    private PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public MessageResponseDTO createPerson(PersonDTO personDTO){

        Person personToSave = personMapper.toModel(personDTO);


        Person savedPerson = personRepository.save(personToSave);
        return MessageResponseDTO
                .builder()
                .message("Created person with ID "+savedPerson.getId())
                .build();
    }

    public List<PersonDTO> listAll() {
        List<Person> people = personRepository.findAll();
        return people.stream()
            //    .map(person -> personMapper.toDTO(person)) versao original
                .map(personMapper::toDTO)
                .collect(Collectors.toList());
    }
}
