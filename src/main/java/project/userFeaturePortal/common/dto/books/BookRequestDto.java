package project.userFeaturePortal.common.dto.books;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BookRequestDto {

    public String titel;
    public Integer erscheinungsjahr;
    public String actor;
}
