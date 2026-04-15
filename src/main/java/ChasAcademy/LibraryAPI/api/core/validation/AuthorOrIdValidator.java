package ChasAcademy.LibraryAPI.api.core.validation;

import ChasAcademy.LibraryAPI.api.core.dto.AuthorReferenceDTO;
import ChasAcademy.LibraryAPI.api.v1.dto.NewBookRequestDTOv1;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AuthorOrIdValidator
        implements ConstraintValidator<ValidAuthorReference, AuthorReferenceDTO> {

    @Override
    public boolean isValid(AuthorReferenceDTO dto, ConstraintValidatorContext context) {
        if (dto == null) return true;

        return dto.name() != null || dto.id() != null;
    }
}