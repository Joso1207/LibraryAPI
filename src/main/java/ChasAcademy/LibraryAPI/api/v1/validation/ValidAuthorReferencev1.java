package ChasAcademy.LibraryAPI.api.v1.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AuthorOrIdValidatorv1.class)
public @interface ValidAuthorReferencev1 {

    String message() default "Either author name or existing authorID must be provided";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}