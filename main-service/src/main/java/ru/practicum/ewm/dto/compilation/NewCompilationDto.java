package ru.practicum.ewm.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.dto.compilation.constraint.NewCompilationConstraint;
import ru.practicum.ewm.dto.compilation.constraint.UpdateCompilationConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    private boolean pinned;
    @NotBlank(groups = NewCompilationConstraint.class)
    @Size(min = 1, max = 50, groups = {NewCompilationConstraint.class, UpdateCompilationConstraint.class})
    private String title;
    private List<Long> events = new ArrayList<>();
}
