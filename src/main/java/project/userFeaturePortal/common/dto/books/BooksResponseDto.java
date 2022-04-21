package project.userFeaturePortal.common.dto.books;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.userFeaturePortal.model.entity.Book;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BooksResponseDto {
  List<Book> result;
  String returnMessage;
}
