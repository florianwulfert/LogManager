package project.logManager.common.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class UserRequestDto {
    public String actor;
    public String name;
    public double height;
    public double weight;
    public String birthdate;
    public String favouriteColor;

    public LocalDate getBirthdateAsLocalDate() {
        String[] bd =  this.birthdate.split("\\.");
        return LocalDate.of(Integer.parseInt(bd[2]), Integer.parseInt(bd[1]), Integer.parseInt(bd[0]));
    }
}
