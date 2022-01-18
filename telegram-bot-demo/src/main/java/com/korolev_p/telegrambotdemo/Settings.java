package com.korolev_p.telegrambotdemo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Settings {

    public Settings(int min, int max, int listCount) {
        this.min = min;
        this.max = max;
        this.listCount = listCount;
    }

    int min;

    int max;

    int listCount;
}
