package project.userFeaturePortal.model.mapper;

import org.mapstruct.Mapper;
import project.userFeaturePortal.common.dto.books.BookDto;
import project.userFeaturePortal.model.entity.Book;

@Mapper(componentModel = "spring")
public interface BookDtoMapper {
  BookDto bookToBookDto(Book book);
}
