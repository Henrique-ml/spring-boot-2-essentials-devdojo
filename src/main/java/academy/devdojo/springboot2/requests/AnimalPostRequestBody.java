package academy.devdojo.springboot2.requests;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.URL;

import lombok.Data;

@Data
public class AnimalPostRequestBody {

	@NotEmpty(message = "The animal name cannot be empty")
	private String name;
	@URL(message = "The URL is not valid")
	private String url;
}
