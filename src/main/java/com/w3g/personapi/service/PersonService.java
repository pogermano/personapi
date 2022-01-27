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
        MessageResponseDTO messageResponse =  createMessageResponse("Created person with ID ", savedPerson.getId()) ;
        return messageResponse;
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
        return personMapper.toDTO(person);


    }

     public void deleteById(Long id)  throws PersonNotFoundException {
        verifyIfExists(id);
        personRepository.deleteById(id);
    }

    public MessageResponseDTO update(Long id, PersonDTO personDTO) throws PersonNotFoundException {
        personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));

        Person updatedPerson = personMapper.toModel(personDTO);
        Person savedPerson = personRepository.save(updatedPerson);

        MessageResponseDTO messageResponse = createMessageResponse("Person successfully updated with ID ", savedPerson.getId());

        return messageResponse;
    }

    private Person verifyIfExists(Long id) throws PersonNotFoundException {
        return personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
    }

    private MessageResponseDTO createMessageResponse(String s, Long id2) {
        return MessageResponseDTO.builder()
                .message(s + id2)
                .build();
    }

}
