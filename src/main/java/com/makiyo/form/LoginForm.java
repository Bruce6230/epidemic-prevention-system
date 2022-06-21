package com.makiyo.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author makiyo
 * @create 2022-06-21 22:11
 */
@Data
@ApiModel
public class LoginForm {
    @NotBlank(message = "临时授权不能为空")
    private String code;
}
