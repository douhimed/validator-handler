package org.mdouhi.app;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class ValidatorTest {

    private static Person validPerson;
    private static Person invalidPerson;

    @BeforeClass
    public static void beforeClass() {
        validPerson = Person.builder().name("mdouhi").age(19).build();
        invalidPerson = Person.builder().name(" ").age(0).build();
    }

    @Test
    public void test_ValidCase(){

        Person person = Validator.validate(p -> p.getName() != null && p.getName().trim().length() != 0, "Name must be not be blank")
                .then(p -> p.getAge() > 0, "Age must be greater than 0")
                .then(p -> p.getAge() < 100, "Age must be less than 100")
                .on(validPerson)
                .validate();

        assertEquals(person, validPerson);

    }

    @Test
    public void test_InvalideCase(){

        try {

            Validator.validate(p -> p.getName() != null && p.getName().trim().length() != 0, Messages.NAME_INVALID)
                    .then(p -> p.getAge() > 0 && p.getAge() < 100, Messages.AGE_INVALIDE)
                    .on(invalidPerson)
                    .validate();

        } catch (Exception e) {
            assertTrue(e instanceof Validator.ValidatorException);
            assertEquals(2, e.getSuppressed().length);
            assertEquals(Messages.NOT_VALID, e.getMessage());
            assertEquals(Messages.NAME_INVALID, e.getSuppressed()[0].getMessage());
            assertEquals(Messages.AGE_INVALIDE, e.getSuppressed()[1].getMessage());
        }
    }
}
