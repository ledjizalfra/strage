package it.buniva.strage.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.buniva.strage.utility.date.MyCustomDeserializerInstant;
import it.buniva.strage.utility.date.MyCustomSerializerInstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data @AllArgsConstructor @NoArgsConstructor
public class ApiResponseCustom {

	@JsonDeserialize(using = MyCustomDeserializerInstant.class)
	@JsonSerialize(using = MyCustomSerializerInstant.class)
	private Instant timestamp;

	private int httpStatusCode;

	private HttpStatus httpStatus;
	
	private String error;
	
	private Object message;
	
	private String path;

}
