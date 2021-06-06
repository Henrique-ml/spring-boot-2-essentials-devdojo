package academy.devdojo.springboot2.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimalPutRequestBody {

	@Schema(description = "This is the Animal's identification that will be updated", example = "1",  required = true)
	private Long id;
	@Schema(description = "This is the Animal's new name", example = "Golden retriever", required = true)
	private String name;
}
