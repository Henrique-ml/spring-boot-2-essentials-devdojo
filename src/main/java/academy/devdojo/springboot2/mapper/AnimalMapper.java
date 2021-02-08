package academy.devdojo.springboot2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import academy.devdojo.springboot2.entities.Animal;
import academy.devdojo.springboot2.requests.AnimalPostRequestBody;
import academy.devdojo.springboot2.requests.AnimalPutRequestBody;

@Mapper(componentModel = "spring")
public abstract class AnimalMapper {

	public static final AnimalMapper INSTANCE = Mappers.getMapper(AnimalMapper.class);
	
	public abstract Animal toAnimal(AnimalPostRequestBody animalPostRequestBody);
	public abstract Animal toAnimal(AnimalPutRequestBody animalPutRequestBody);
}
