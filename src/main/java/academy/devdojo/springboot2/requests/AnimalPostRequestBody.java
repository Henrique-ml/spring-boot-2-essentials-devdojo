package academy.devdojo.springboot2.requests;

import javax.validation.constraints.NotEmpty;

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
	private String name;
}
