package ChasAcademy.LibraryAPI.api.core;

public record ApiResponseWrapper<T>(
        T data,
        String version
) {}
