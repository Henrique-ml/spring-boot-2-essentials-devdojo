package academy.devdojo.springboot2.integrations;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import academy.devdojo.springboot2.AnimalPostRequestBodyCreator;
import academy.devdojo.springboot2.entities.Animal;
import academy.devdojo.springboot2.entities.DevDojoUser;
import academy.devdojo.springboot2.repositories.AnimalRepository;
import academy.devdojo.springboot2.repositories.DevDojoUserRepository;
import academy.devdojo.springboot2.requests.AnimalPostRequestBody;
import academy.devdojo.springboot2.utils.AnimalCreator;
import academy.devdojo.springboot2.wrappers.PageableResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AnimalControllerIT {
	
	@Autowired
	@Qualifier(value = "testRestTemplateRoleUser")
	private TestRestTemplate testRestTemplateRoleUser;
	@Autowired
	@Qualifier(value = "testRestTemplateRoleAdmin")
	private TestRestTemplate testRestTemplateRoleAdmin;
	
	@Autowired
	private AnimalRepository animalRepository;
	
	@Autowired
	private DevDojoUserRepository devDojoUserRepository;
	
	private static final DevDojoUser USER = DevDojoUser.builder()
			.name("DevDojo Academy")
			.password("{bcrypt}$2a$10$ROeCah/PSiaYJbWi4SQhoe33aH4CRL6smkW3Bp4fxbyecSgyVgviG")
			.username("devdojo")
			.authorities("ROLE_USER")
			.build();
	
	private static final DevDojoUser ADMIN = DevDojoUser.builder()
			.name("Henrique Lira")
			.password("{bcrypt}$2a$10$ROeCah/PSiaYJbWi4SQhoe33aH4CRL6smkW3Bp4fxbyecSgyVgviG")
			.username("rique")
			.authorities("ROLE_USER,ROLE_ADMIN")
			.build();
	
	@TestConfiguration
	@Lazy
	static class Config {
		
		@Bean(name = "testRestTemplateRoleUser")
		public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
			
			RestTemplateBuilder restTemplateBuilder	= new RestTemplateBuilder() 
					.rootUri("http://localhost:" + port)
					.basicAuthentication("devdojo", "academy");
			
			return new TestRestTemplate(restTemplateBuilder);
		}

		@Bean(name = "testRestTemplateRoleAdmin")
		public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port) {
			
			RestTemplateBuilder restTemplateBuilder	= new RestTemplateBuilder() 
					.rootUri("http://localhost:" + port)
					.basicAuthentication("rique", "academy");
			
			return new TestRestTemplate(restTemplateBuilder);
		}
	}
	
	@Test
	@DisplayName("list returns a list of animal inside page object when successful")
	void list_ReturnsListOfAnimaisInsidePageObject_WhenSuccessful() {
		Animal savedAnimal = animalRepository.save(AnimalCreator.createAnimalToBeSaved());
		devDojoUserRepository.save(USER);
		
		String expectedName = savedAnimal.getName();

		PageableResponse<Animal> animalPage = testRestTemplateRoleUser.exchange("/animais", HttpMethod.GET, null,
				new ParameterizedTypeReference<PageableResponse<Animal>>() {}).getBody();

		Assertions.assertThat(animalPage).isNotNull();

		Assertions.assertThat(animalPage.toList()).isNotEmpty().hasSize(1);

		Assertions.assertThat(animalPage.toList().get(0).getName()).isEqualTo(expectedName);
	}

	@Test
	@DisplayName("listAll returns a list of animal when successful")
	void listAll_ReturnsListOfAnimais_WhenSuccessful() {
		Animal savedAnimal = animalRepository.save(AnimalCreator.createAnimalToBeSaved());
		devDojoUserRepository.save(USER);

		String expectedName = savedAnimal.getName();

		List<Animal> animais = testRestTemplateRoleUser.exchange("/animais/all", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Animal>>() {}).getBody();

		Assertions.assertThat(animais).isNotNull().isNotEmpty().hasSize(1);

		Assertions.assertThat(animais.get(0).getName()).isEqualTo(expectedName);
	}

	@Test
	@DisplayName("findById returns animal when successful")
	void findById_ReturnsAnimal_WhenSuccessful() {
		Animal savedAnimal = animalRepository.save(AnimalCreator.createAnimalToBeSaved());
		devDojoUserRepository.save(USER);

		Long expectedId = savedAnimal.getId();
		
		Animal animal = testRestTemplateRoleUser.getForObject("/animais/{id}", Animal.class, expectedId);

		Assertions.assertThat(animal).isNotNull();

		Assertions.assertThat(animal.getId()).isNotNull().isEqualTo(expectedId);
	}

	@Test
	@DisplayName("findByName returns a list animal when successful")
	void findByName_ReturnsListOfAnimal_WhenSuccessful() {
		Animal savedAnimal = animalRepository.save(AnimalCreator.createAnimalToBeSaved());
		devDojoUserRepository.save(USER);

		String expectedName = savedAnimal.getName();
		
		String url = String.format("/animais/find?name=%s", expectedName);
		
		List<Animal> animais = testRestTemplateRoleUser.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Animal>>() {}).getBody();
		
		Assertions.assertThat(animais).isNotNull().isNotEmpty().hasSize(1);

		Assertions.assertThat(animais.get(0).getName()).isNotNull().isEqualTo(expectedName);
	}

	@Test
	@DisplayName("findByName returns an empty list animal when animal is not found")
	void findByName_ReturnsEmptyListOfAnimal_WhenAnimalIsNotFound() {
		devDojoUserRepository.save(USER);

		List<Animal> animais = testRestTemplateRoleUser.exchange("/animais/find?name=qualquer nome", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Animal>>() {}).getBody();
		
		Assertions.assertThat(animais).isNotNull().isEmpty();
	}

	@Test
	@DisplayName("save returns animal when successful")
	void save_ReturnsAnimal_WhenSuccessful() {
		devDojoUserRepository.save(USER);

		AnimalPostRequestBody animalPostRequestBody = AnimalPostRequestBodyCreator.createAnimalPostRequestBody();
		
		ResponseEntity<Animal> animalResponseEntity = testRestTemplateRoleUser.postForEntity("/animais", animalPostRequestBody, Animal.class);
		
		Assertions.assertThat(animalResponseEntity).isNotNull();
		Assertions.assertThat(animalResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Assertions.assertThat(animalResponseEntity.getBody()).isNotNull();
		Assertions.assertThat(animalResponseEntity.getBody().getId()).isNotNull();
	}
	
	@Test
	@DisplayName("delete removes animal when successful")
	void delete_RemovesAnimal_WhenSuccessful() {
		Animal savedAnimal = animalRepository.save(AnimalCreator.createAnimalToBeSaved());
		devDojoUserRepository.save(ADMIN);

		savedAnimal.setName("new name");
		
		ResponseEntity<Void> animalResponseEntity = testRestTemplateRoleAdmin.exchange("/animais/admin/{id}",
				HttpMethod.DELETE, null, Void.class, savedAnimal.getId());
		
		Assertions.assertThat(animalResponseEntity).isNotNull();
		Assertions.assertThat(animalResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
	@Test
	@DisplayName("delete returns 403 when user is not admin")
	void delete_Returns403_WhenUserIsNotAdmin() {
		Animal savedAnimal = animalRepository.save(AnimalCreator.createAnimalToBeSaved());
		devDojoUserRepository.save(USER);
		
		savedAnimal.setName("new name");
		
		ResponseEntity<Void> animalResponseEntity = testRestTemplateRoleUser.exchange("/animais/admin/{id}",
				HttpMethod.DELETE, null, Void.class, savedAnimal.getId());
		
		Assertions.assertThat(animalResponseEntity).isNotNull();
		Assertions.assertThat(animalResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}
	
	@Test
	@DisplayName("replace updates animal when successful")
	void replace_UpdateAnimal_WhenSuccessful() {
		Animal savedAnimal = animalRepository.save(AnimalCreator.createAnimalToBeSaved());
		devDojoUserRepository.save(USER);

		savedAnimal.setName("new name");
		
		ResponseEntity<Void> animalResponseEntity = testRestTemplateRoleUser.exchange("/animais",
				HttpMethod.PUT,new HttpEntity<>(savedAnimal), Void.class);
		
		Assertions.assertThat(animalResponseEntity).isNotNull();
		Assertions.assertThat(animalResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
}
