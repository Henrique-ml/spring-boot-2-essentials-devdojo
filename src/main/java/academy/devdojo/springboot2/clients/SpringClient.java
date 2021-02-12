package academy.devdojo.springboot2.clients;

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
	}
}
