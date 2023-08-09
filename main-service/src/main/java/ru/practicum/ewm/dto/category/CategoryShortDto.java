package ru.practicum.ewm.dto.category;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryShortDto {
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}