package com.yunhui.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DriverClassEnum {


    MYSQL(1,"com.mysql.jdbc.Driver"),
    MSSQL(2,"com.microsoft.jdbc.sqlserver.SQLServerDriver"),

    ;

    private Integer type;
    private String name;



    public static String getDriverClassByType(Integer type){
        for (DriverClassEnum value : values()) {
            if(value.type.equals(type)){
                return value.name;
            }
        }
        return null;
    }
}
