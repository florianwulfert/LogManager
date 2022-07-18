package project.userFeaturePortal.common.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetFavouriteBookResponseDto {
    String favouriteBook;
    String returnMessage;
}
