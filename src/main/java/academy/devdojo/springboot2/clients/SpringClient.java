package academy.devdojo.springboot2.clients;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import academy.devdojo.springboot2.entities.Animal;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SpringClient {
	public static void main(String[] args) {
		
		ResponseEntity<Animal> entity = new RestTemplate().getForEntity("http://localhost:8080/animais/{id}", Animal.class, 2);
		log.info(entity);
		
		Animal object = new RestTemplate().getForObject("http://localhost:8080/animais/{id}", Animal.class, 2);
		log.info(object);
		
		Animal[] animais = new RestTemplate().getForObject("http://localhost:8080/animais/all", Animal[].class);
		log.info(Arrays.toString(animais));

		ResponseEntity<List<Animal>> exchange = new RestTemplate().exchange("http://localhost:8080/animais/all",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Animal>>() {});
		log.info(exchange.getBody());
		
		Animal shihtzu = Animal.builder().name("Shih-tzu").build();
		Animal shihtzuSaved = new RestTemplate().postForObject("http://localhost:8080/animais", shihtzu, Animal.class);
		log.info("saved animal {}", shihtzuSaved);
		
		Animal pitbull = Animal.builder().name("Pitbull").build();
		ResponseEntity<Animal> pitbullSaved = new RestTemplate().exchange("http://localhost:8080/animais",
				HttpMethod.POST,
				new HttpEntity<>(pitbull, createJsonHeader()), 
				Animal.class);
		log.info("saved animal {}", pitbullSaved);
		
		Animal animalToBeUpdated = pitbullSaved.getBody();
		animalToBeUpdated.setName("Pitbull 2");
		ResponseEntity<Void> pitbullUpdated = new RestTemplate().exchange("http://localhost:8080/animais",
				HttpMethod.PUT,
				new HttpEntity<>(animalToBeUpdated, createJsonHeader()), 
				Void.class);
		log.info(pitbullUpdated);
		
		ResponseEntity<Void> pitbullDeleted = new RestTemplate().exchange("http://localhost:8080/animais/{id}",
				HttpMethod.DELETE,
				null,
				Void.class, 
				animalToBeUpdated.getId());
		log.info(pitbullDeleted);
	}
	
	private static HttpHeaders createJsonHeader() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return httpHeaders;
	}
}
