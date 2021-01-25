package academy.devdojo.springboot2.services;

import java.util.List;

import org.springframework.stereotype.Service;

import academy.devdojo.springboot2.entities.Animal;

@Service
public class AnimalService {

	// private final AnimalRepository animalRepository;
	
	public List<Animal> listAll() {
        return List.of(new Animal(1L, "Bulldog"), new Animal(2L, "Pug"));
	}
}
