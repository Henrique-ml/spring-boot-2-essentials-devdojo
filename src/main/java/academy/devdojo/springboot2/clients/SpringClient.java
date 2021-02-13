package academy.devdojo.springboot2.clients;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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
	}
}
