package academy.devdojo.springboot2.controllers;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import academy.devdojo.springboot2.entities.Animal;
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
	}
	
	@Test
	@DisplayName("List returns list of animal inside page object when successful")
	void list_ReturnsListOfAnimaisInsidePageObject_WhenSuccessful () {
		String expectedName = AnimalCreator.createValidAnimal().getName();
		
		Page<Animal> animePage = animalController.list(null).getBody();
		
		Assertions.assertThat(animePage).isNotNull();
		
		Assertions.assertThat(animePage.toList())
				.isNotEmpty()
				.hasSize(1);
		
		Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
	}

}
