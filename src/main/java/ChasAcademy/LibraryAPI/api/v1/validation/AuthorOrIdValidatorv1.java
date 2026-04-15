package ChasAcademy.LibraryAPI.api.v1.validation;

import ChasAcademy.LibraryAPI.api.v1.dto.NewBookRequestDTOv1;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AuthorOrIdValidatorv1
        implements ConstraintValidator<ValidAuthorReferencev1, NewBookRequestDTOv1> {

    @Override
    public boolean isValid(NewBookRequestDTOv1 dto, ConstraintValidatorContext context) {
        if (dto == null) return true;

        return dto.author() != null || dto.authorID() != null;
    }
}