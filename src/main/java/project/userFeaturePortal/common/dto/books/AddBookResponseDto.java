package project.userFeaturePortal.common.dto.books;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.userFeaturePortal.model.entity.Book;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddBookResponseDto {
    Book book;
}
