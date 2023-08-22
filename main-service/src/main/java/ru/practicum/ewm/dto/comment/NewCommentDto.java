package ru.practicum.ewm.dto.comment;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class NewCommentDto {
    @NotBlank
    @Size(min = 3, max = 7000)
    private String text;
}