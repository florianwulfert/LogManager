package project.logManager.common.dto;


import lombok.Builder;
import lombok.Data;

import java.time.DateTimeException;
import java.time.LocalDate;

import static project.logManager.common.message.ErrorMessages.ILLEGAL_BIRTHDATE_FORMAT;

@Builder
@Data
public class UserRequestDto {
    public String actor;
    public String name;
    public String birthdate;
    public Double weight;
    public Double height;
    public String favouriteColor;

    public LocalDate getBirthdateAsLocalDate() {
        try{
            String[] bd =  this.birthdate.split("\\.");
            return LocalDate.of(Integer.parseInt(bd[2]), Integer.parseInt(bd[1]), Integer.parseInt(bd[0]));
        } catch (IndexOutOfBoundsException | DateTimeException e) {
            throw new RuntimeException(ILLEGAL_BIRTHDATE_FORMAT);
        }

    }
}
