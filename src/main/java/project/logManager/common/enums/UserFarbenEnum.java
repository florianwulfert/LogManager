package project.logManager.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum UserFarbenEnum {
    BLAU("blau"), ROT("rot"), ORANGE("orange"), GELB("gelb"), SCHWARZ("schwarz");


    @Setter
    @Getter
    public String farbe;

}
