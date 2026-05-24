package ChasAcademy.LibraryAPI.api.core;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(List<T> content, int page, int size, long totalElements) {


    public PageResponse(Page<T> page){
        this(page.getContent(),page.getNumber(),page.getSize(),page.getTotalElements());
    }

    public PageResponse(List<T> content, int page, int size, long totalElements) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
    }
}
