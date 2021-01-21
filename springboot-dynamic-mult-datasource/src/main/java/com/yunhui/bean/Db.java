package com.yunhui.bean;

import lombok.Data;

@Data
public class Db {
    private Long id;
    private String key;
    private String url;
    private String user;
    private String passwd;
    private Integer type;
}
