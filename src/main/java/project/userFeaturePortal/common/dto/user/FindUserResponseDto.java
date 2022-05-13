package project.userFeaturePortal.common.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.userFeaturePortal.model.entity.User;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindUserResponseDto {
    User foundUser;
}
