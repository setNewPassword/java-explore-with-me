package ru.practicum.ewm.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRequestDto {
    private Boolean pinned;
    @Size(min = 1, max = 50)
    private String title;
    private List<Long> events;
}