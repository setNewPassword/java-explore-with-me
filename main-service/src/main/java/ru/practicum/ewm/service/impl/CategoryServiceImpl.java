package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;
import ru.practicum.ewm.exception.CategoryIsNotEmptyException;
import ru.practicum.ewm.exception.CategoryNameAlreadyExistException;
import ru.practicum.ewm.exception.CategoryNotExistException;
import ru.practicum.ewm.exception.CategoryNotFoundException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.service.CategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto addNewCategory(NewCategoryDto newCategoryDto) {
        if (categoryRepository.existsByName(newCategoryDto.getName())) {
            throw new CategoryNameAlreadyExistException("Категория с таким названием уже существует!");
        }
        return categoryMapper.toCategoryDto(categoryRepository
                .save(categoryMapper.newCategoryDtoToCategory(newCategoryDto)));
    }

    @Override
    public void deleteCategory(Long catId) {
        if (eventRepository.existsByCategoryId(catId)) {
            throw new CategoryIsNotEmptyException("Невозможно удалить категорию — она используется в событиях.");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() ->
                        new CategoryNotFoundException(String.format("Не найдена категория с id = %d.", catId)));
        if (categoryRepository.existsByName(categoryDto.getName())
                && !category.getName().equals(categoryDto.getName())) {
            throw new CategoryNameAlreadyExistException("Категория с таким названием уже существует!");
        }

        categoryDto.setId(catId);
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.categoryDtoToCategory(categoryDto)));
    }

    @Override
    public List<CategoryDto> getCategories(Pageable pageable) {

        return categoryMapper.toCategoryDtoList(categoryRepository.findAll(pageable).toList());
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() ->
                        new CategoryNotExistException(String.format("Не найдена категория с id = %d.", catId)));
        return categoryMapper.toCategoryDto(category);
    }
}