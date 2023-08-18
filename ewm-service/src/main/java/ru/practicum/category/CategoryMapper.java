package ru.practicum.category;

import org.mapstruct.Mapper;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto categoryToDto(Category category);

    Category newCategoryDto(NewCategoryDto newCategoryDto);

    List<CategoryDto> listCategoryToListDto(List<Category> categories);
}
