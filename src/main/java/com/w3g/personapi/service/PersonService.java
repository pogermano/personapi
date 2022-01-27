package com.w3g.personapi.service;

import com.w3g.personapi.dto.request.PersonDTO;
import com.w3g.personapi.dto.response.MessageResponseDTO;
import com.w3g.personapi.entity.Person;
import com.w3g.personapi.exception.PersonNotFoundException;
import com.w3g.personapi.mapper.PersonMapper;
import com.w3g.personapi.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public PersonDTO findById(Long id)  throws PersonNotFoundException {
        Person person = verifyIfExists(id);
        // versao normal, sem simplificar
       // Optional<Person> person = personRepository.findById(id);
       // if(person.isEmpty()){
       //     throw new PersonNotFoundException(id);
      //  }
      //  PersonDTO personToReturn = personMapper.toDTO(person.get());
        PersonDTO personToReturn = personMapper.toDTO(person);
        return personToReturn;

    }

     public void deleteById(Long id)  throws PersonNotFoundException {
        verifyIfExists(id);
        personRepository.deleteById(id);
    }



    private Person verifyIfExists(Long id) throws PersonNotFoundException {
        return personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
    }
}
