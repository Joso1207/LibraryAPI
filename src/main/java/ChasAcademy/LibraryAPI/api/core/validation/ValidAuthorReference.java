package ChasAcademy.LibraryAPI.api.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AuthorOrIdValidator.class)
public @interface ValidAuthorReference {

    String message() default "Either author name or existing authorID must be provided";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}