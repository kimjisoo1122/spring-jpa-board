package com.example.board.config;

import com.example.board.entity.Category;
import com.example.board.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitCategory {

    private final InitCategoryService initCategoryService;

//    @Profile("local")
//    @PostConstruct
//    public void init() {
//        initCategoryService.init();
//    }
//    @Profile("test")
//    @PostConstruct
//    public void initForTest() {
//        initCategoryService.init();
//    }

    @Service
    @Transactional
    @RequiredArgsConstructor
    static class InitCategoryService {

        private final CategoryService categoryService;

        public void init() {
            Category parentCategory = Category.createCategory("커뮤니티");
            Long parentId = categoryService.save(parentCategory, null);
            Category childCategory1 = Category.createCategory("질문");
            Category childCategory2 = Category.createCategory("공유");
            Category childCategory3 = Category.createCategory("잡담");
            categoryService.save(childCategory1, parentId);
            categoryService.save(childCategory2, parentId);
            categoryService.save(childCategory3, parentId);
        }
    }

}
