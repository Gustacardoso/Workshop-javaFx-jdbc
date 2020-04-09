package model.exception;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	//vamos usar o  map, relembrnado que ele usa doi parametros
	//umm é o caminho e outro é o tipo
	public Map<String, String> errors = new HashMap<>();
	
	public ValidationException(String msg) {
		super(msg);
	}
	
	public Map<String , String> getErrors() {
		return errors;
	}
	
	public void addError(String fielName, String errorMensage) {
		     errors.put(fielName, errorMensage);
	}

}
