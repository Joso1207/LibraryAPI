package ChasAcademy.LibraryAPI.api.core.dto;

import lombok.Builder;

@Builder
public record AuthorDTO(String name,Long id,int writtenWorksAmount) { }
