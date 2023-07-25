package telran.validation.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import telran.validation.annotation.Pattern;
import telran.validation.annotation.Range;

public class RangeValidator implements Validator {

	@Override
	public String validate(Annotation ann, Field field, Object obj) throws Exception {
		Range range = (Range) ann;
		int value = (int) field.get(obj);
		return (value <= range.max() && value >= range.min()) ? "" : String.format("%s", range.errorMessage());
	}
}
