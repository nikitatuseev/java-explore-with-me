package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.dto.UpdateCategoryDto;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        Category newCategory = categoryMapper.newCategoryDto(newCategoryDto);
        return categoryMapper.categoryToDto(categoryRepository.save(newCategory));
    }

    @Override
    @Transactional
    public void deleteCategory(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с ID %d не найдена", categoryId));

        int eventCountInCategory = eventRepository.countByCategoryId(categoryId);

        if (eventCountInCategory > 0) {
            throw new IllegalStateException("Невозможно удалить категорию, так как есть связанные события");
        }

        categoryRepository.deleteById(categoryId);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Integer categoryId, UpdateCategoryDto updateCategoryDto) {
        Category categoryToUpdate = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с ID %d не найдено", categoryId));

        if (updateCategoryDto.getName() != null) {
            categoryToUpdate.setName(updateCategoryDto.getName());
        }
        categoryRepository.save(categoryToUpdate);

        return categoryMapper.categoryToDto(categoryToUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        int page = from > 0 ? from / size : 0;
        Pageable categoryPageable = PageRequest.of(page, size);
        Page<Category> categoryPage = categoryRepository.findAll(categoryPageable);
        return new ArrayList<>(categoryMapper.listCategoryToListDto(categoryPage.getContent()));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Integer categoryId) {
        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с ID %s не найдено", categoryId));
        return categoryMapper.categoryToDto(category);
    }
}
