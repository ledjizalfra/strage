package it.buniva.strage.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class ApiErrorsView {

	List<ApiFieldError> fieldErrors;

	List<ApiGlobalError> globalErrors;
}
