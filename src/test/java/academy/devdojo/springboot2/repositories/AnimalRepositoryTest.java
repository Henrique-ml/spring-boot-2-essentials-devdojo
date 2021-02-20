package academy.devdojo.springboot2.repositories;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import academy.devdojo.springboot2.entities.Animal;
import academy.devdojo.springboot2.utils.AnimalCreator;

@DataJpaTest
@DisplayName("Tests for Animal Repository")
class AnimalRepositoryTest {

	@Autowired
	private AnimalRepository animalRepository;
	
	@Test
	@DisplayName("Save persists animal when Successful")
	void save_PersistAnimal_WhenSuccessful() {
		Animal animalToBeSaved = AnimalCreator.createAnimalToBeSaved();
		
		Animal animalSaved = this.animalRepository.save(animalToBeSaved);
		
		Assertions.assertThat(animalSaved).isNotNull();
		
		Assertions.assertThat(animalSaved.getId()).isNotNull();
		
		Assertions.assertThat(animalSaved.getName()).isEqualTo(animalToBeSaved.getName());
	}
	
	@Test
	@DisplayName("Save updates animal when Successful")
	void save_UpdatesAnimal_WhenSuccessful() {
		Animal animalToBeSaved = AnimalCreator.createAnimalToBeSaved();
		
		Animal animalSaved = this.animalRepository.save(animalToBeSaved);
		
		animalSaved.setName("Caramelo mais caramelado");
		
		Animal animalUpdated = this.animalRepository.save(animalSaved);
		
		Assertions.assertThat(animalUpdated).isNotNull();
		
		Assertions.assertThat(animalUpdated.getId()).isNotNull();
		
		Assertions.assertThat(animalUpdated.getName()).isEqualTo(animalSaved.getName());
	}
	
	@Test
	@DisplayName("Delete removes animal when Successful")
	void delete_RemovesAnimal_WhenSuccessful() {
		Animal animalToBeSaved = AnimalCreator.createAnimalToBeSaved();
		
		Animal animalSaved = this.animalRepository.save(animalToBeSaved);
		
		this.animalRepository.delete(animalSaved);
		
		Optional<Animal> animalOptional = this.animalRepository.findById(animalSaved.getId());
		
		Assertions.assertThat(animalOptional).isEmpty();
	}
	
	@Test
	@DisplayName("Find By Name returns list of animal when Successful")
	void findByName_ReturnsListOfAnimal_WhenSuccessful() {
		Animal animalToBeSaved = AnimalCreator.createAnimalToBeSaved();
		
		Animal animalSaved = this.animalRepository.save(animalToBeSaved);
		
		String name = animalSaved.getName();
		
		List<Animal> animais = this.animalRepository.findByName(name);
		
		Assertions.assertThat(animais)
				.isNotEmpty()
				.contains(animalSaved);
	 }
	
	@Test
	@DisplayName("Find By Name returns empty list when no animal is found")
	void findByName_ReturnsEmptyList_WhenAnimalIsNotFound() {
		List<Animal> animais = this.animalRepository.findByName("Bom de guerra II");
		
		Assertions.assertThat(animais).isEmpty();
	}
	
	@Test
	@DisplayName("Save throw ConstraintViolationException when name is empty")
	void save_ThrowsConstraintViolationException_WhenNameIsEmpty() {
		Animal animal = new Animal();
		
//		Assertions.assertThatThrownBy(() -> this.animalRepository.save(animal))
//				.isInstanceOf(ConstraintViolationException.class);
		
		Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> this.animalRepository.save(animal))
			.withMessageContaining("The animal name cannot be empty");
	}
}
