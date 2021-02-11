package academy.devdojo.springboot2.controllers;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import academy.devdojo.springboot2.entities.Animal;
import academy.devdojo.springboot2.requests.AnimalPostRequestBody;
import academy.devdojo.springboot2.requests.AnimalPutRequestBody;
import academy.devdojo.springboot2.services.AnimalService;
import academy.devdojo.springboot2.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/animais")
@Log4j2
@RequiredArgsConstructor
public class AnimalController {
	
    private final DateUtil dateUtil;
    private final AnimalService animalService;

    @GetMapping
    public ResponseEntity<List<Animal>> list() {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(animalService.listAll());
    }
    
    @GetMapping(path = "/{id}")
    public ResponseEntity<Animal> finById(@PathVariable long id) {
        return ResponseEntity.ok(animalService.findByIdOrThrowBadRequestException(id));
    }
    
    @GetMapping(path = "/find")
    public ResponseEntity<List<Animal>> finByName(@RequestParam String name) {
    	return ResponseEntity.ok(animalService.findByName(name));
    }
    
    @PostMapping
    public ResponseEntity<Animal> save(@RequestBody @Valid AnimalPostRequestBody animalPostRequestBody) {
    	return new ResponseEntity<>(animalService.save(animalPostRequestBody), HttpStatus.CREATED);
    }
    
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
    	animalService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody AnimalPutRequestBody animalPutRequestBody){
    	animalService.replace(animalPutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
