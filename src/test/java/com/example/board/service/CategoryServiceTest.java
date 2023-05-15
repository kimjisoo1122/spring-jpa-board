package com.example.board.service;

import com.example.board.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;

    @Test
    void register() {
        // 테스트에서 진행하는 각각의 메소드는 개별 트랜잭션임. -> 서로 영속컨텍스트공유 X
        // given
        Category parentCategory = new Category("커뮤니티");

        // when
        categoryService.save(parentCategory, null);
        // then
        Category category =
                categoryService.findById(parentCategory.getId()).
                        orElseThrow(IllegalArgumentException::new);
        assertEquals(parentCategory.getName(), category.getName());

        // 자식카테고리
        // given
        Long parentCategoryId = parentCategory.getId();
        Category category1 = new Category("질문");
        Category category2 = new Category("공유");
        Category category3 = new Category("잡담");

        // when
        categoryService.save(category1, parentCategoryId);
        categoryService.save(category2, parentCategoryId);
        categoryService.save(category3, parentCategoryId);

        String c1 = categoryService.findById(category1.getId()).map(Category::getName).orElse("");
        String c2 = categoryService.findById(category2.getId()).map(Category::getName).orElse("");
        String c3 = categoryService.findById(category3.getId()).map(Category::getName).orElse("");

        // then
        assertEquals(c1, category1.getName());
        assertEquals(c2, category2.getName());
        assertEquals(c3, category3.getName());
    }

}