package org.mdouhi.app;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author mdouhi
 * <p>
 * Validator using Java 8 features (Function, Supplier, default methods, ...)
 */
interface Validator {

    Function<String, ValidatorSupplier> not_valid = messageError -> {
        RuntimeException exception = new ValidatorException(Messages.NOT_VALID);
        exception.addSuppressed(new RuntimeException(messageError));
        throw exception;
    };

    ValidatorSupplier on(Person person);

    default Validator then(Predicate<Person> predicate, String messageError) {

        return person -> {
            try {
                on(person).validate();
                return predicate.test(person)
                        ? () -> person
                        : not_valid.apply(messageError);

            } catch (ValidatorException ex) {
                if (!predicate.test(person))
                    ex.addSuppressed(new ValidatorException(messageError));
                throw ex;
            }
        };
    }

    static Validator validate(Predicate<Person> predicate, String messageError) {
        return person -> predicate.test(person)
                ? () -> person
                : not_valid.apply(messageError);
    }

    interface ValidatorSupplier extends Supplier<Person> {
        default Person validate() {
            return get();
        }
    }

    class ValidatorException extends RuntimeException {
        public ValidatorException(String messageError) {
            super(messageError);
        }
    }
}