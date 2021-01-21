package com.yunhui.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WordTypeEnum {


    FOURTH(1),


    KAOYAN(5),

    ;

    private Integer key;


    public static WordTypeEnum getInstance(Integer key) {
        for (WordTypeEnum value : values()) {
            if (value.key.equals(key)) {
                return value;

            }
        }
        return null;
    }

}
