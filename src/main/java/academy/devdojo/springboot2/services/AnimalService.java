package academy.devdojo.springboot2.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import academy.devdojo.springboot2.entities.Animal;
import academy.devdojo.springboot2.exceptions.BadRequestException;
import academy.devdojo.springboot2.mappers.AnimalMapper;
import academy.devdojo.springboot2.repositories.AnimalRepository;
import academy.devdojo.springboot2.requests.AnimalPostRequestBody;
import academy.devdojo.springboot2.requests.AnimalPutRequestBody;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimalService {

	private final AnimalRepository animalRepository;

	public Page<Animal> listAll(Pageable pegeable) {
		return animalRepository.findAll(pegeable);
	}
	
	public List<Animal> findByName(String name) {
		return animalRepository.findByName(name);
	}

	public Animal findByIdOrThrowBadRequestException(long id) {
		return animalRepository.findById(id)
				.orElseThrow(() -> new BadRequestException("Animal not Found"));
	}

	@Transactional
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
