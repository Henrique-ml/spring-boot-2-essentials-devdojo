package academy.devdojo.springboot2.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import academy.devdojo.springboot2.entities.Animal;

@DataJpaTest
@DisplayName("Tests for Animal Repository")
class AnimalRepositoryTest {

	@Autowired
	private AnimalRepository animalRepository;
	
	@Test
	@DisplayName("Save creates animal when Successful")
	void save_PersistAnimal_WhenSuccessful() {
		Animal animalToBeSaved = createAnimal();
		Animal animalSaved = this.animalRepository.save(animalToBeSaved);
		Assertions.assertThat(animalSaved).isNotNull();
		Assertions.assertThat(animalSaved.getId()).isNotNull();
		Assertions.assertThat(animalSaved.getName()).isEqualTo(animalToBeSaved.getName());
	}
	
	private Animal createAnimal() {
		return Animal.builder()
				.name("Caramelo")
				.build();
	}

}
