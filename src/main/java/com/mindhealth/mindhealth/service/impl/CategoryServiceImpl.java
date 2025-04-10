package com.mindhealth.mindhealth.service.impl;

import com.mindhealth.mindhealth.domain.Category;
import com.mindhealth.mindhealth.model.CategoryDTO;
import com.mindhealth.mindhealth.repository.CategoryRepository;
import com.mindhealth.mindhealth.repository.EventRepository;
import com.mindhealth.mindhealth.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EventRepository eventRepository;

    @Override
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> mapToDTO(category))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO findById(Long id) {
        return categoryRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    @Override
    public CategoryDTO save(CategoryDTO categoryDTO) {
        Category category = new Category();
        mapToEntity(categoryDTO, category);
        Category savedCategory = categoryRepository.save(category);
        return mapToDTO(savedCategory);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    private CategoryDTO mapToDTO(Category category) {
        return new CategoryDTO(
            category.getId(),
            category.getName(),
            category.getDescription(),
            category.getImageUrl()
        );
    }

    private void mapToEntity(CategoryDTO dto, Category entity) {
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setImageUrl(dto.imageUrl());
    }
} 