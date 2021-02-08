package academy.devdojo.springboot2.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import academy.devdojo.springboot2.entities.Animal;
import academy.devdojo.springboot2.mapper.AnimalMapper;
import academy.devdojo.springboot2.repositories.AnimalRepository;
import academy.devdojo.springboot2.requests.AnimalPostRequestBody;
import academy.devdojo.springboot2.requests.AnimalPutRequestBody;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimalService {

	private final AnimalRepository animalRepository;

	public List<Animal> listAll() {
		return animalRepository.findAll();
	}

	public Animal findByIdOrThrowBadRequestException(long id) {
		return animalRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Animal not Found"));
	}

	public Animal save(AnimalPostRequestBody animalPostRequestBody) {
		return animalRepository.save(AnimalMapper.INSTANCE.toAnimal(animalPostRequestBody));
	}

	public void delete(long id) {
		animalRepository.delete(findByIdOrThrowBadRequestException(id));
	}

	public void replace(AnimalPutRequestBody animalPutRequestBody) {
		Animal savedAnimal = findByIdOrThrowBadRequestException(animalPutRequestBody.getId());
		Animal animal = AnimalMapper.INSTANCE.toAnimal(animalPutRequestBody);
		animal.setId(savedAnimal.getId());	
		animalRepository.save(animal);
	}
}
