package academy.devdojo.springboot2;

import academy.devdojo.springboot2.requests.AnimalPutRequestBody;
import academy.devdojo.springboot2.utils.AnimalCreator;

public class AnimalPutRequestBodyCreator {

	public static AnimalPutRequestBody createAnimalPutRequestBody() {
		return AnimalPutRequestBody.builder()
				.id(AnimalCreator.createValidUpdatedAnimal().getId())
				.name(AnimalCreator.createValidUpdatedAnimal().getName())
				.build();
	}
}
