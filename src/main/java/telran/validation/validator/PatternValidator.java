package telran.validation.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import telran.validation.annotation.Pattern;

public class PatternValidator implements Validator {

	@Override
	public String validate(Annotation ann, Field field, Object obj) throws Exception {
		Pattern pattern = (Pattern) ann;
		String value = field.get(obj).toString();
		return value.matches(pattern.regex()) ? "" : pattern.errorMessage();
	}

}
