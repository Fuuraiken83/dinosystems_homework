package homework.web;

import homework.PhoneNumber;
import homework.data.JdbcPhoneBookRepository;
import homework.data.JdbcUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class PhoneBookController {

    @Autowired
    private JdbcPhoneBookRepository jdbcPhonebookRepository;
    @Autowired
    private JdbcUserRepository jdbcUserRepository;

    public PhoneBookController(JdbcPhoneBookRepository jdbcPhonebookRepository, JdbcUserRepository jdbcUserRepository){
        this.jdbcPhonebookRepository = jdbcPhonebookRepository;
        this.jdbcUserRepository = jdbcUserRepository;
    }

    @PostMapping(value = "/phonebook")
    @ResponseBody
    public ResponseEntity<PhoneNumber> addPhonebook(@Valid @RequestBody PhoneNumber phonenumber, Errors errors){
        if (errors.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            return ResponseEntity.ok(jdbcPhonebookRepository.add(phonenumber));
        }
        catch(DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping(value = "/phonebook")
    @ResponseBody
    public ResponseEntity<PhoneNumber> updatePhonebook(@Valid @RequestBody PhoneNumber phonenumber, Errors errors){
        try {
            if (errors.hasErrors()|| jdbcPhonebookRepository.update(phonenumber) == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            return ResponseEntity.ok(phonenumber);
        }
        catch(DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping(value = "/phonebook")
    @ResponseBody
    public ResponseEntity<PhoneNumber> deletePhonebook(@Valid @RequestBody PhoneNumber phonenumber, Errors errors){

        if (errors.hasErrors() || jdbcPhonebookRepository.delete(phonenumber) == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(phonenumber);
    }

    @GetMapping(value = "/phonebook", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity<Iterable<PhoneNumber>> getAllPhonebooks() {
        return ResponseEntity.ok(jdbcPhonebookRepository.findAll());
    }

    @GetMapping(value = "/phonebook/{phonebookId}", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity<PhoneNumber> getPhonebook(@PathVariable("phonebookId") long id){
        try {
            return ResponseEntity.ok(jdbcPhonebookRepository.findById(id));
    }
        catch(EmptyResultDataAccessException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(value = "user/{id}/phonebook", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity<Iterable<PhoneNumber>> getPhonebooksByOwner(@PathVariable("id") long id){
        try{
            jdbcUserRepository.findById(id);
            if (jdbcPhonebookRepository.findByOwnerId(id).iterator().hasNext()){
                return ResponseEntity.ok(jdbcPhonebookRepository.findByOwnerId(id));
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }
        catch(EmptyResultDataAccessException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @GetMapping(value = "phonebook/",params = {"number"}, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity<Iterable<PhoneNumber>> getPhonebooksByNumber(@RequestParam String number){
        if (jdbcPhonebookRepository.findByNumber(number).iterator().hasNext()){
            return ResponseEntity.ok(jdbcPhonebookRepository.findByNumber(number));
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
