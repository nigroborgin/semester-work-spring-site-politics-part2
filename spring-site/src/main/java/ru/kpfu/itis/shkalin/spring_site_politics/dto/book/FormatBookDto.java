package ru.kpfu.itis.shkalin.spring_site_politics.dto.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormatBookDto {
    private String formatName;
    private String url;
}
