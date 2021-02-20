package academy.devdojo.springboot2;

import academy.devdojo.springboot2.requests.AnimalPostRequestBody;
import academy.devdojo.springboot2.utils.AnimalCreator;

public class AnimalPostRequestBodyCreator {

	public static AnimalPostRequestBody createAnimalPostRequestBody() {
		return AnimalPostRequestBody.builder()
				.name(AnimalCreator.createAnimalToBeSaved().getName())
				.build();
	}
}
