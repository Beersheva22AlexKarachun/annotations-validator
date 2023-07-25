package telran.validation.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import telran.validation.validator.PatternValidator;

@Retention(RUNTIME)
@Target(FIELD)
@Validation(validator = PatternValidator.class)
public @interface Pattern {
	String regex();

	String errorMessage() default "value mismatches the pattern";

}
