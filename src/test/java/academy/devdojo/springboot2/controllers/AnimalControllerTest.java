package academy.devdojo.springboot2.controllers;

import java.util.Collections;
import java.util.List;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import academy.devdojo.springboot2.AnimalPostRequestBodyCreator;
import academy.devdojo.springboot2.AnimalPutRequestBodyCreator;
import academy.devdojo.springboot2.entities.Animal;
import academy.devdojo.springboot2.requests.AnimalPostRequestBody;
import academy.devdojo.springboot2.requests.AnimalPutRequestBody;
import academy.devdojo.springboot2.services.AnimalService;
import academy.devdojo.springboot2.utils.AnimalCreator;

@ExtendWith(SpringExtension.class)
class AnimalControllerTest {

	@InjectMocks
	private AnimalController animalController;
	@Mock
	private AnimalService animalServiceMock;

	@BeforeEach
	void setUp() {
		PageImpl<Animal> animalPage = new PageImpl<>(List.of(AnimalCreator.createValidAnimal()));
		BDDMockito.when(animalServiceMock.listAll(ArgumentMatchers.any()))
				.thenReturn(animalPage);

		BDDMockito.when(animalServiceMock.listAllNonPageable())
				.thenReturn(List.of(AnimalCreator.createValidAnimal()));

		BDDMockito.when(animalServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
				.thenReturn((AnimalCreator.createValidAnimal()));

		BDDMockito.when(animalServiceMock.findByName(ArgumentMatchers.anyString()))
				.thenReturn(List.of(AnimalCreator.createValidAnimal()));
		
		BDDMockito.when(animalServiceMock.save(ArgumentMatchers.any(AnimalPostRequestBody.class)))
				.thenReturn(AnimalCreator.createValidAnimal());
		
		BDDMockito.doNothing().when(animalServiceMock).delete(ArgumentMatchers.anyLong());
		
		BDDMockito.doNothing().when(animalServiceMock).replace(ArgumentMatchers.any(AnimalPutRequestBody.class));
	}

	@Test
	@DisplayName("list returns a list of animal inside page object when successful")
	void list_ReturnsListOfAnimaisInsidePageObject_WhenSuccessful() {
		String expectedName = AnimalCreator.createValidAnimal().getName();

		Page<Animal> animalPage = animalController.list(null).getBody();

		Assertions.assertThat(animalPage).isNotNull();

		Assertions.assertThat(animalPage.toList()).isNotEmpty().hasSize(1);

		Assertions.assertThat(animalPage.toList().get(0).getName()).isEqualTo(expectedName);
	}

	@Test
	@DisplayName("listAll returns a list of animal when successful")
	void listAll_ReturnsListOfAnimais_WhenSuccessful() {
		String expectedName = AnimalCreator.createValidAnimal().getName();

		List<Animal> animais = animalController.listAll().getBody();

		Assertions.assertThat(animais).isNotNull().isNotEmpty().hasSize(1);

		Assertions.assertThat(animais.get(0).getName()).isEqualTo(expectedName);
	}

	@Test
	@DisplayName("findById returns animal when successful")
	void finById_ReturnsAnimal_WhenSuccessful() {
		Long expectedId = AnimalCreator.createValidAnimal().getId();

		Animal animal = animalController.findById(1).getBody();

		Assertions.assertThat(animal).isNotNull();

		Assertions.assertThat(animal.getId()).isNotNull().isEqualTo(expectedId);
	}

	@Test
	@DisplayName("findByName returns a list animal when successful")
	void finByName_ReturnsListOfAnimal_WhenSuccessful() {
		String expectedName = AnimalCreator.createValidAnimal().getName();

		List<Animal> animais = animalController.findByName("Bom de guerra III").getBody();

		Assertions.assertThat(animais).isNotNull().isNotEmpty().hasSize(1);

		Assertions.assertThat(animais.get(0).getName()).isNotNull().isEqualTo(expectedName);
	}

	@Test
	@DisplayName("findByName returns an empty list animal when animal is not found")
	void finByName_ReturnsEmptyListOfAnimal_WhenAnimalIsNotFound() {
		BDDMockito.when(animalServiceMock.findByName(ArgumentMatchers.anyString()))
				.thenReturn(Collections.emptyList());
		
		List<Animal> animais = animalController.findByName("Bom de guerra III").getBody();
		
		Assertions.assertThat(animais).isNotNull().isEmpty();
	}
	
	@Test
	@DisplayName("save returns animal when successful")
	void save_ReturnsAnimal_WhenSuccessful() {
		Animal animal = animalController.save(AnimalPostRequestBodyCreator.createAnimalPostRequestBody()).getBody();

		Assertions.assertThat(animal).isNotNull().isEqualTo(AnimalCreator.createValidAnimal());
	}
	
	@Test
	@DisplayName("delete removes animal when successful")
	void delete_RemovesAnimal_WhenSuccessful() {
		Assertions.assertThatCode(() -> animalController.delete(1))
				.doesNotThrowAnyException();
		
		ResponseEntity<Void> entity = animalController.delete(1);
		
		Assertions.assertThat(entity).isNotNull();
		
		Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
	
	@Test
	@DisplayName("replace update animal when successful")
	void replace_UpdateAnimal_WhenSuccessful() {
		Assertions.assertThatCode(() -> animalController.replace(AnimalPutRequestBodyCreator.createAnimalPutRequestBody()))
				.doesNotThrowAnyException();
		
		ResponseEntity<Void> entity = animalController.replace(AnimalPutRequestBodyCreator.createAnimalPutRequestBody());
		
		Assertions.assertThat(entity).isNotNull();
		
		Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
}
