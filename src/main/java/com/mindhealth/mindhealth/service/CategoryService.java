package com.mindhealth.mindhealth.service;

import com.mindhealth.mindhealth.domain.Category;
import com.mindhealth.mindhealth.domain.Event;
import com.mindhealth.mindhealth.model.CategoryDTO;
import com.mindhealth.mindhealth.repository.CategoryRepository;
import com.mindhealth.mindhealth.repository.EventRepository;
import com.mindhealth.mindhealth.util.NotFoundException;
import com.mindhealth.mindhealth.util.ReferencedWarning;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final MeterRegistry meterRegistry;

    public CategoryService(final CategoryRepository categoryRepository,
            final EventRepository eventRepository,
            final MeterRegistry meterRegistry) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
        this.meterRegistry = meterRegistry;
    }

    @Cacheable(cacheNames = "categories")
    public List<CategoryDTO> findAll() {
        final List<Category> categories = categoryRepository.findAllByActiveTrueOrderByDisplayOrderAscNameAsc();
        return categories.stream()
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .toList();
    }

    public CategoryDTO get(final Long id) {
        return categoryRepository.findById(id)
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CategoryDTO categoryDTO) {
        final Category category = new Category();
        mapToEntity(categoryDTO, category);
        meterRegistry.counter("categories.create.requests").increment();
        Long id = categoryRepository.save(category).getId();
        evictCategoryCache();
        return id;
    }

    public void update(final Long id, final CategoryDTO categoryDTO) {
        final Category category = categoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(categoryDTO, category);
        meterRegistry.counter("categories.update.requests").increment();
        categoryRepository.save(category);
        evictCategoryCache();
    }

    public void delete(final Long id) {
        meterRegistry.counter("categories.delete.requests").increment();
        categoryRepository.deleteById(id);
        evictCategoryCache();
    }

    private CategoryDTO mapToDTO(final Category category, final CategoryDTO categoryDTO) {
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

    private Category mapToEntity(final CategoryDTO categoryDTO, final Category category) {
        category.setName(categoryDTO.getName());
        return category;
    }

    @CacheEvict(cacheNames = "categories", allEntries = true)
    public void evictCategoryCache() {
        // cache eviction marker
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Category category = categoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Event categoryEvent = eventRepository.findFirstByCategory(category);
        if (categoryEvent != null) {
            referencedWarning.setKey("category.event.category.referenced");
            referencedWarning.addParam(categoryEvent.getId());
            return referencedWarning;
        }
        return null;
    }

}
