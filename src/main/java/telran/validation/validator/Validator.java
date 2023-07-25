package telran.validation.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public interface Validator {
	String validate(Annotation ann, Field field, Object obj) throws Exception;
}
