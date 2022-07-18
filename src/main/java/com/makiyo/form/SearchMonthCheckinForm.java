package com.makiyo.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class SearchMonthCheckinForm {
    @NotNull
    @Range(min=2000,max = 2100)
    private Integer year;

    @NotNull
    @Range(min=1,max = 12)
    private Integer month;
}
