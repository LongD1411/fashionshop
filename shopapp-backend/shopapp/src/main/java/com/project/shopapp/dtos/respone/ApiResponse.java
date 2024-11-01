package com.project.shopapp.dtos.respone;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T>{
    @Builder.Default
    private int code = 1000;
    private String message;
    private T result;
    private List<T> results;

    private Integer totalPage;
    private Integer totalItem;
}
