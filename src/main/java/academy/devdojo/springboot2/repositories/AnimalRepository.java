package academy.devdojo.springboot2.repositories;

import java.util.List;

import academy.devdojo.springboot2.entities.Animal;

public interface AnimalRepository {
	
	List<Animal> listAll();

}
