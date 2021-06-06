package academy.devdojo.springboot2.requests;

import javax.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor 
public class AnimalPostRequestBody {

	@NotEmpty(message = "The animal name cannot be empty")
	@Schema(description = "This is the Animal's name", example = "Pug", required = true)
	private String name;
}
