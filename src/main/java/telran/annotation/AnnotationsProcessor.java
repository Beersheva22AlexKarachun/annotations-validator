package telran.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import telran.validation.annotation.Pattern;
import telran.validation.annotation.Validation;
import telran.validation.validator.Validator;

public class AnnotationsProcessor {
	public static final String VALIDATOR = "Validator";
	public static final String VALIDATOR_PACKAGE = "validator";
	public static final String NO_ID_MESSAGE = "The class must have a field annotated with @Id";
	public static final String SEVERAL_ID_MESSAGE = "The class must have only one field annotated with @Id";

	public static Object getId(Object obj) throws IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		Field field = getFieldId(fields);
		return field.get(obj);
	}

	private static Field getFieldId(Field[] fields) {
		Field res = null;
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.isAnnotationPresent(Id.class)) {
				if (res != null) {
					throw new IllegalArgumentException(SEVERAL_ID_MESSAGE);
				}
				res = field;
			}
		}
		if (res == null) {
			throw new IllegalArgumentException(NO_ID_MESSAGE);
		}
		return res;
	}

	public static List<String> validate(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		List<String> errorMessages = new ArrayList<String>();
		Arrays.stream(fields).forEach(field -> validateField(field, errorMessages, obj));
		return errorMessages;
	}

	private static void validateField(Field field, List<String> errorMessages, Object obj) {
		field.setAccessible(true);
		List<Annotation> validateAnnotations = Arrays.stream(field.getAnnotations())
				.filter(ann -> ann.annotationType().isAnnotationPresent(Validation.class)).toList();

		validateAnnotations.stream().forEach(ann -> {
			try {
				validateAnnotation(ann, field, errorMessages, obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private static void validateAnnotation(Annotation annotation, Field field, List<String> errorMessages, Object obj)
			throws Exception {
		Validator validator = getValidatorByAnnotation(annotation);
		String message = validator.validate(annotation, field, obj);
		if (!message.isEmpty()) {
			errorMessages.add(message);
		}
	}

	private static Validator getValidatorByAnnotation(Annotation annotation) throws Exception {
		Class<?> validatorClazz = annotation.annotationType().getAnnotation(Validation.class).validator();
		return (Validator) validatorClazz.getConstructor().newInstance();
	}

	private static Validator getValidatorByName(Annotation annotation) throws Exception {
		String[] fullName = annotation.annotationType().getName().split("\\.");
		String annotationName = fullName[fullName.length - 1];

		String validatorName = String.join(".", Arrays.copyOfRange(fullName, 0, fullName.length - 2));
		validatorName += String.format(".%s.%s%s", VALIDATOR_PACKAGE, annotationName, VALIDATOR);
		Validator validator = (Validator) Class.forName(validatorName).getConstructor().newInstance();
		return validator;
	}

	private static String getMessage(Field field, Object obj) {
		String res = "";
		field.setAccessible(true);
		try {
			String value = field.get(obj).toString();
			Pattern pattern = field.getAnnotation(Pattern.class);
			String regex = pattern.regex();
			res = value.matches(regex) ? res : pattern.errorMessage();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			res = e.getMessage();
		}
		return res;
	}
}
