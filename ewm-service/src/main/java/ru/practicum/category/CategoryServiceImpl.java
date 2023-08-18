package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        Category newCategory = categoryMapper.newCategoryDto(newCategoryDto);
        return categoryMapper.categoryToDto(categoryRepository.save(newCategory));
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Категория с ID %s не найдено",categoryId));
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public CategoryDto updateCategory(Integer categoryId, NewCategoryDto newCategoryDto) {
        Category categoryToUpdate = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с ID %s не найдено",categoryId));
        Category newCategory = categoryMapper.newCategoryDto(newCategoryDto);
        newCategory.setId(categoryId);
        categoryRepository.save(newCategory);
        return categoryMapper.categoryToDto(newCategory);
    }

    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        int page = from > 0 ? from / size : 0;
        Pageable categoryPageable = PageRequest.of(page, size);
        Page<Category> categoryPage = categoryRepository.findAll(categoryPageable);
        return new ArrayList<>(categoryMapper.listCategoryToListDto(categoryPage.getContent()));
    }

    @Override
    public CategoryDto getCategoryById(Integer categoryId) {
        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с ID %s не найдено",categoryId));
        return categoryMapper.categoryToDto(category);
    }
}
