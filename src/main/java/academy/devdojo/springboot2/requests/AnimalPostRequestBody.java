package academy.devdojo.springboot2.requests;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class AnimalPostRequestBody {

	@NotEmpty(message = "The animal name cannot be empty")
	private String name;
}
