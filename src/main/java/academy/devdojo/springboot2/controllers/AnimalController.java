package academy.devdojo.springboot2.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import academy.devdojo.springboot2.entities.Animal;
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
    public List<Animal> list(){
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return animalService.listAll();
    }

}
