package com.makiyo.form;

import lombok.Data;

@Data
public class CheckinForm {
    //地址
    private String address;
    //国家
    private String country;
    //省份
    private String province;
    //城市
    private String city;
    //区域
    private String district;
}
