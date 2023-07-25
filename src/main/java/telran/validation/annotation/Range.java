package telran.validation.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import telran.validation.validator.RangeValidator;

@Retention(RUNTIME)
@Target(FIELD)
@Validation(validator = RangeValidator.class)
public @interface Range {
	int min();
	int max();
	String errorMessage() default "value's out of range";
}
