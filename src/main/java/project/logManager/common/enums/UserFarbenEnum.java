package project.logManager.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum UserFarbenEnum {
    BLAU("blau"), ROT("rot"), ORANGE("orange"), GELB("gelb"), SCHWARZ("schwarz");

    @Getter
    public final String farbe;
}
