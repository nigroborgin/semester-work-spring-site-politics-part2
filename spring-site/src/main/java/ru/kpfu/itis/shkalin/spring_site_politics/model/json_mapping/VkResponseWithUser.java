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
public class VkResponseWithUser {
    @JsonProperty("response")
    private VkUser[] users;
}
