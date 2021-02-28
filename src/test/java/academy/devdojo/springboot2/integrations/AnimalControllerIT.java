package academy.devdojo.springboot2.integrations;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import academy.devdojo.springboot2.entities.Animal;
import academy.devdojo.springboot2.repositories.AnimalRepository;
import academy.devdojo.springboot2.utils.AnimalCreator;
import academy.devdojo.springboot2.wrappers.PageableResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class AnimalControllerIT {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private AnimalRepository animalRepository;
	
	@Test
	@DisplayName("list returns a list of animal inside page object when successful")
	void list_ReturnsListOfAnimaisInsidePageObject_WhenSuccessful() {
		Animal savedAnimal = animalRepository.save(AnimalCreator.createAnimalToBeSaved());
		
		String expectedName = savedAnimal.getName();

		PageableResponse<Animal> animalPage = testRestTemplate.exchange("/animais", HttpMethod.GET, null,
				new ParameterizedTypeReference<PageableResponse<Animal>>() {}).getBody();

		Assertions.assertThat(animalPage).isNotNull();

		Assertions.assertThat(animalPage.toList()).isNotEmpty().hasSize(1);

		Assertions.assertThat(animalPage.toList().get(0).getName()).isEqualTo(expectedName);
	}

}
