package academy.devdojo.springboot2.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import academy.devdojo.springboot2.entities.Animal;
import academy.devdojo.springboot2.requests.AnimalPostRequestBody;
import academy.devdojo.springboot2.requests.AnimalPutRequestBody;
import academy.devdojo.springboot2.services.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/animais")
@RequiredArgsConstructor
public class AnimalController {

	private final AnimalService animalService;

	@GetMapping
	@Operation(
			summary = "List all animais paginated", 
			description = "The default size is 20, use the parameter 'size' to change the default value",
			tags = {"animal"})
	public ResponseEntity<Page<Animal>> list(@ParameterObject Pageable pegeable) {
		return ResponseEntity.ok(animalService.listAll(pegeable));
	}

	@GetMapping(path = "/all")
	public ResponseEntity<List<Animal>> listAll() {
		return ResponseEntity.ok(animalService.listAllNonPageable());
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<Animal> findById(@PathVariable long id) {
		return ResponseEntity.ok(animalService.findByIdOrThrowBadRequestException(id));
	}

	@GetMapping(path = "by-id/{id}")
	public ResponseEntity<Animal> findByIdAuthenticationPrincipal(@PathVariable long id,
			@AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.ok(animalService.findByIdOrThrowBadRequestException(id));
	}

	@GetMapping(path = "/find")
	public ResponseEntity<List<Animal>> findByName(@RequestParam String name) {
		return ResponseEntity.ok(animalService.findByName(name));
	}

	@PostMapping
	public ResponseEntity<Animal> save(@RequestBody @Valid AnimalPostRequestBody animalPostRequestBody) {
		return new ResponseEntity<>(animalService.save(animalPostRequestBody), HttpStatus.CREATED);
	}

	@DeleteMapping(path = "/admin/{id}")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Successful Operation"),
			@ApiResponse(responseCode = "400", description = "When Animal Does Not Exist In The Database")
	})
	public ResponseEntity<Void> delete(@PathVariable long id) {
		animalService.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping
	public ResponseEntity<Void> replace(@RequestBody AnimalPutRequestBody animalPutRequestBody) {
		animalService.replace(animalPutRequestBody);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
