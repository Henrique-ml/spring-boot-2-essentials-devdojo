package academy.devdojo.springboot2.requests;

import lombok.Data;

@Data
public class AnimalPutRequestBody {

	private Long id;
	private String name;
}
