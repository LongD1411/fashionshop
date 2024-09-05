package com.project.shopapp.respone;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseList {
    @JsonProperty("users")
    private List<UserResponse> users;
    @JsonProperty("total_item")
    private  int totalItem;
    @JsonProperty("total_page")
    private  int totalPage;
}
