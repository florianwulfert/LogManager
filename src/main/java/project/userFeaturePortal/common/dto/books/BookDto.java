package project.userFeaturePortal.common.dto.books;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    String titel;
    int erscheinungsjahr;
}
