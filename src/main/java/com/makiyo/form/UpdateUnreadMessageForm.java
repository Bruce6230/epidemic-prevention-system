package com.makiyo.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class UpdateUnreadMessageForm {
    @NotNull
    private String id;
}
