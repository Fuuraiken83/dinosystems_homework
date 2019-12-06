package homework.web;

import homework.User;
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
public class UserController {

    @Autowired
    private JdbcUserRepository jdbcUserRepository;

    public UserController(JdbcUserRepository jdbcUserRepository){
        this.jdbcUserRepository = jdbcUserRepository;
    }

    @PostMapping(value = "/user", consumes = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity<User> addUser(@Valid @RequestBody User user, Errors errors){
        try {
            if (errors.hasErrors()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            return ResponseEntity.ok(jdbcUserRepository.add(user));
        }
        catch(DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping(value = "/user", consumes = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user,Errors errors){
        try {
            if (errors.hasErrors()|| jdbcUserRepository.update(user) == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            return ResponseEntity.ok(user);
        }
        catch(DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @DeleteMapping(value = "/user", consumes = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity<User> deleteUser(@Valid @RequestBody User user,Errors errors) {
        if (errors.hasErrors() || jdbcUserRepository.delete(user) == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(user);

    }

    @GetMapping(value = "/user", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity<Iterable<User>> getAllUsers(){
        return ResponseEntity.ok(jdbcUserRepository.findAll());
    }

    @GetMapping(value = "/user/{userId}",produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity<User> getUser(@PathVariable("userId") long id){
        try {
            return ResponseEntity.ok(jdbcUserRepository.findById(id));
        }
        catch(EmptyResultDataAccessException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(value = "/user",params = {"name"},produces = "application/json;charset=utf-8")
    @ResponseBody
    public Iterable<User> getUsersWithName(@RequestParam String name){
        return jdbcUserRepository.findByName(name);
    }
}
