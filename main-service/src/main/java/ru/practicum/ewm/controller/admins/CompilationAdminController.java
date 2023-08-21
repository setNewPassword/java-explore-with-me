package ru.practicum.ewm.controller.admins;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.group.NewGroup;
import ru.practicum.ewm.dto.group.UpdateGroup;
import ru.practicum.ewm.service.CompilationService;

import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(path = "/admin/compilations")
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@Validated(NewGroup.class)
                                             @RequestBody NewCompilationDto newCompilationDto) {
        return compilationService.addCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@NotNull @PathVariable Long compId,
                                            @Validated(UpdateGroup.class)
                                            @RequestBody NewCompilationDto updateCompilationRequestDto) {
        return compilationService.updateCompilation(compId, updateCompilationRequestDto);
    }
}