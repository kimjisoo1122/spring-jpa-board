package com.example.board.repository;

import com.example.board.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final EntityManager em;

    public void save(Category category) {
        em.persist(category);
    }

    public Optional<Category> findById(Long categoryId) {
        return Optional.ofNullable(em.find(Category.class, categoryId));
    }

    public Optional<Category> findByName(String categoryName) {
        List<Category> categories = em.createQuery("select c from Category c where c.name = :name", Category.class)
                .setParameter("name", categoryName)
                .getResultList();
        return categories.isEmpty() ? Optional.empty() : Optional.of(categories.get(0));
    }
}
