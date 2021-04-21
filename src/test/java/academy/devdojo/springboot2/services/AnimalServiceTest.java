package academy.devdojo.springboot2.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import academy.devdojo.springboot2.AnimalPostRequestBodyCreator;
import academy.devdojo.springboot2.AnimalPutRequestBodyCreator;
import academy.devdojo.springboot2.entities.Animal;
import academy.devdojo.springboot2.exceptions.BadRequestException;
import academy.devdojo.springboot2.repositories.AnimalRepository;
import academy.devdojo.springboot2.utils.AnimalCreator;

@ExtendWith(SpringExtension.class)
class AnimalServiceTest {

	@InjectMocks
	private AnimalService animalService;
	@Mock
	private AnimalRepository animalRepositoryMock;

	@BeforeEach
	void setUp() {
		PageImpl<Animal> animalPage = new PageImpl<>(List.of(AnimalCreator.createValidAnimal()));
		BDDMockito.when(animalRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
				.thenReturn(animalPage);

		BDDMockito.when(animalRepositoryMock.findAll())
				.thenReturn(List.of(AnimalCreator.createValidAnimal()));

		BDDMockito.when(animalRepositoryMock.findById(ArgumentMatchers.anyLong()))
				.thenReturn(Optional.of(AnimalCreator.createValidAnimal()));

		BDDMockito.when(animalRepositoryMock.findByName(ArgumentMatchers.anyString()))
				.thenReturn(List.of(AnimalCreator.createValidAnimal()));
		
		BDDMockito.when(animalRepositoryMock.save(ArgumentMatchers.any(Animal.class)))
				.thenReturn(AnimalCreator.createValidAnimal());
		
		BDDMockito.doNothing().when(animalRepositoryMock).delete(ArgumentMatchers.any(Animal.class));
	}

	@Test
	@DisplayName("listAll returns a list of animal inside page object when successful")
	void listAll_ReturnsListOfAnimaisInsidePageObject_WhenSuccessful() {
		String expectedName = AnimalCreator.createValidAnimal().getName();

		Page<Animal> animalPage = animalService.listAll(PageRequest.of(1, 1));

		Assertions.assertThat(animalPage).isNotNull();

		Assertions.assertThat(animalPage.toList()).isNotEmpty().hasSize(1);

		Assertions.assertThat(animalPage.toList().get(0).getName()).isEqualTo(expectedName);
	}

	@Test
	@DisplayName("listAllNonPageable returns a list of animal when successful")
	void listAllNonPageable_ReturnsListOfAnimais_WhenSuccessful() {
		String expectedName = AnimalCreator.createValidAnimal().getName();

		List<Animal> animais = animalService.listAllNonPageable();

		Assertions.assertThat(animais).isNotNull().isNotEmpty().hasSize(1);

		Assertions.assertThat(animais.get(0).getName()).isEqualTo(expectedName);
	}
	
	@Test
	@DisplayName("findByName returns a list animal when successful")
	void findByName_ReturnsListOfAnimal_WhenSuccessful() {
		String expectedName = AnimalCreator.createValidAnimal().getName();

		List<Animal> animais = animalService.findByName("Bom de guerra IV");

		Assertions.assertThat(animais).isNotNull().isNotEmpty().hasSize(1);

		Assertions.assertThat(animais.get(0).getName()).isNotNull().isEqualTo(expectedName);
	}
	
	@Test
	@DisplayName("findByName returns an empty list animal when animal is not found")
	void findByName_ReturnsEmptyListOfAnimal_WhenAnimalIsNotFound() {
		BDDMockito.when(animalRepositoryMock.findByName(ArgumentMatchers.anyString()))
				.thenReturn(Collections.emptyList());
		
		List<Animal> animais = animalService.findByName("Bom de guerra IV");
		
		Assertions.assertThat(animais).isNotNull().isEmpty();
	}

	@Test
	@DisplayName("findByIdOrThrowBadRequestException returns animal when successful")
	void findByIdOrThrowBadRequestException_ReturnsAnimal_WhenSuccessful() {
		Long expectedId = AnimalCreator.createValidAnimal().getId();

		Animal animal = animalService.findByIdOrThrowBadRequestException(1);

		Assertions.assertThat(animal).isNotNull();

		Assertions.assertThat(animal.getId()).isNotNull().isEqualTo(expectedId);
	}
	
	@Test
	@DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when animal is not found")
	void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenAnimalIsNotFound() {
		BDDMockito.when(animalRepositoryMock.findById(ArgumentMatchers.anyLong()))
				.thenReturn(Optional.empty());
		
		Assertions.assertThatExceptionOfType(BadRequestException.class)
				.isThrownBy(() -> animalService.findByIdOrThrowBadRequestException(1))
				.withMessageContaining("Animal not found");
;
	}

	@Test
	@DisplayName("save returns animal when successful")
	void save_ReturnsAnimal_WhenSuccessful() {
		Animal animal = animalService.save(AnimalPostRequestBodyCreator.createAnimalPostRequestBody());

		Assertions.assertThat(animal).isNotNull().isEqualTo(AnimalCreator.createValidAnimal());
	}
	
	@Test
	@DisplayName("delete removes animal when successful")
	void delete_RemovesAnimal_WhenSuccessful() {
		Assertions.assertThatCode(() -> animalService.delete(1))
				.doesNotThrowAnyException();
	}
	
	@Test
	@DisplayName("replace updates animal when successful")
	void replace_UpdateAnimal_WhenSuccessful() {
		Assertions.assertThatCode(() -> animalService.replace(AnimalPutRequestBodyCreator.createAnimalPutRequestBody()))
				.doesNotThrowAnyException();
	}


}
