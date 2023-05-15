package com.example.board.service;

import com.example.board.entity.Category;
import com.example.board.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public void save(Category category, Long parentId) {
        if (parentId != null) {
            this.findById(parentId)
                    .ifPresentOrElse(category::setParent,
                            () -> {
                                throw new IllegalArgumentException("부모 카테고리를 찾을 수 없습니다.");
                            });
        }
        categoryRepository.save(category);
    }

    public Optional<Category> findById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }
    public Optional<Category> findByName(String categoryName) {
        return categoryRepository.findByName(categoryName);
    }
}
