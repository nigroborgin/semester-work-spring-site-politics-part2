package ru.kpfu.itis.shkalin.spring_site_politics.model.json_mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VkUser {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("deactivated")
    private String deactivated;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("can_access_closed")
    private Boolean canAccessClosed;
    @JsonProperty("is_closed")
    private Boolean isClosed;

}
