package com.makiyo.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author Makiyo
 * @date 2022/7/26 23:37
 */
@Data
@ApiModel
public class WebLoginForm {
    @NotBlank(message = "username不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9]{5,20}$",message = "username格式错误")
    private String username;

    @NotBlank(message = "password不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$",message = "password格式错误")
    private String password;
}
