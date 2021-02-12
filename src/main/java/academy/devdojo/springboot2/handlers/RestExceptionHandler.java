package academy.devdojo.springboot2.handlers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import academy.devdojo.springboot2.exceptions.BadRequestException;
import academy.devdojo.springboot2.exceptions.BadRequestExceptionDetails;
import academy.devdojo.springboot2.exceptions.ExceptionDetails;
import academy.devdojo.springboot2.exceptions.ValidationExceptionDetails;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestException(BadRequestException bre) {
		return new ResponseEntity<>(
				BadRequestExceptionDetails.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.title("Bad Request Exception, Check the Documentation")
				.details(bre.getMessage())
				.developerMessage(bre.getClass().getName())
				.build(), HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		List<FieldError> fieldErros = exception.getBindingResult().getFieldErrors();
		
		String fields = fieldErros.stream().map(FieldError::getField).collect(Collectors.joining(", "));
		String fieldsMessage = fieldErros.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

		return new ResponseEntity<>(
				ValidationExceptionDetails.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.title("Bad Request Exception, Invalid Fields")
				.details("Check the field(s) error")
				.developerMessage(exception.getClass().getName())
				.fields(fields)
				.fieldsMessage(fieldsMessage)
				.build(), HttpStatus.BAD_REQUEST); 
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
			Exception exception, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

		ExceptionDetails exceptionDetails = ExceptionDetails.builder()
			.timestamp(LocalDateTime.now())
			.status(status.value())
			.title(exception.getCause().getMessage())
			.details(exception.getMessage())
			.developerMessage(exception.getClass().getName())
			.build();
		
		return new ResponseEntity<>(exceptionDetails, headers, status);
	}

}