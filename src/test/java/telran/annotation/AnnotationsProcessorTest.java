package telran.annotation;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static telran.annotation.AnnotationsProcessor.*;

class AnnotationsProcessorTest {

	@Test
	void getIdTest() throws Exception {
		String email = "qwerty@gmail.com";
		assertEquals(email, getId(new Xright(email, "Xright")));
		assertThrowsExactly(IllegalArgumentException.class, () -> getId(new XnoId(email, "XnoId")), NO_ID_MESSAGE);
		assertThrowsExactly(IllegalArgumentException.class, () -> getId(new XtwoId(email, "XtwoId")),
				SEVERAL_ID_MESSAGE);
	}
}

class Xright {
	public Xright(String email, String name) {
		this.email = email;
		this.name = name;
	}

	@Id
	String email;
	String name;
}

class XnoId {
	public XnoId(String email, String name) {
		this.email = email;
		this.name = name;
	}

	String email;
	String name;
}

class XtwoId {
	public XtwoId(String email, String name) {
		this.email = email;
		this.name = name;
	}

	@Id
	String email;
	@Id
	String name;
}
