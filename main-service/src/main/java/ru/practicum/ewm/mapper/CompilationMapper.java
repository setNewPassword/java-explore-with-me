package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.model.Compilation;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface CompilationMapper {

    CompilationDto toCompilationDto(Compilation savedCompilation);

    List<CompilationDto> toListCompilationDto(List<Compilation> compilations);

}