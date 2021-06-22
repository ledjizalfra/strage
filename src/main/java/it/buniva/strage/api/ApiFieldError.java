package it.buniva.strage.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class ApiFieldError {
	
	private String field;

	private String code;

	private String defaultMessage;

	private Object rejectedValue;

}
