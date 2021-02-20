package academy.devdojo.springboot2.utils;

import academy.devdojo.springboot2.entities.Animal;

public class AnimalCreator {

	public static Animal createAnimalToBeSaved() {
		return Animal.builder()
				.name("Caramelo")
				.build();
	}
	
	public static Animal createValidAnimal() {
		return Animal.builder()
				.id(1L)
				.name("Caramelo")
				.build();
	}
	
	public static Animal createValidUpdatedAnimal() {
		return Animal.builder()
				.name("Caramelo mais caramelado")
				.build();
	}
}
