package academy.devdojo.springboot2.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import academy.devdojo.springboot2.entities.Animal;

@Service
public class AnimalService {

	private static List<Animal> animais;
	
	static {
		animais = new ArrayList<>(List.of(new Animal(1L, "Bulldog"), new Animal(2L, "Pug")));
	}
	
	// private final AnimalRepository animalRepository;
	
	public List<Animal> listAll() {
        return animais;
	}
	
	public Animal findById(long id) {
		return animais.stream()
				.filter(animal -> animal.getId().equals(id))
				.findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Animal not Found"));
	}

	public Animal save(Animal animal) {
		animal.setId(ThreadLocalRandom.current().nextLong(3, 100000));
		animais.add(animal);
		return animal;
	}

	public void delete(long id) {
		animais.remove(findById(id));
	}

	public void replace(Animal animal) {
		delete(animal.getId());
		animais.add(animal);
	}
}
