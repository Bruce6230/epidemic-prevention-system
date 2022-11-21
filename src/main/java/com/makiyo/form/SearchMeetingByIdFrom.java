package com.makiyo.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class SearchMeetingByIdFrom {
    @NotNull
    @Min(1)
    private Integer id;
}
