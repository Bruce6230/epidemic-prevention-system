package com.makiyo.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * @author Makiyo
 * @date 2022/7/21 23:38
 */
@Data
@ApiModel
public class SearchUserGroupByDeptForm {
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{1,15}$")
    private String keyword;
}