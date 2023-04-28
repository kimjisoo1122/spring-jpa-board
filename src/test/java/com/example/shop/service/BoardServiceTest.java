package com.example.shop.service;

import com.example.shop.dto.BoardDTO;
import com.example.shop.dto.MemberDTO;
import com.example.shop.entity.Board;
import com.example.shop.entity.Category;
import com.example.shop.entity.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
//@Transactional
class BoardServiceTest {

    @Autowired
    BoardService boardService;
    @Autowired
    MemberService memberService;
    @Autowired
    CategoryService categoryService;

    @Test
    void 글등록() throws Exception {
        //given
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setName("김지수");
        memberDTO.setPassword("test1234");
        memberDTO.setEmail("test1234@test.com");
        memberDTO.setPhone("010-4953-3653");
        Long memberId = memberService.join(memberDTO);

        Member member = memberService.findById(memberId).orElse(null);
        assertNotNull(member);

        // given
        Category parentCategory = new Category("parentCategory");

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
        Category category1 = new Category("category1");
        Category category2 = new Category("category2");
        Category category3 = new Category("category3");

        // when
        categoryService.save(category1, parentCategoryId);
        categoryService.save(category2, parentCategoryId);
        categoryService.save(category3, parentCategoryId);

        Category findCategory = categoryService.findByName(category1.getName()).orElse(null);
        assertNotNull(findCategory);

        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setContent("test");
        boardDTO.setTitle("test");
        boardDTO.setMemberId(member.getId());
        boardDTO.setCategoryName(category1.getName());

        //when
        Long boardId = boardService.register(boardDTO);
        Board findBoard = boardService.findById(boardId).orElse(null);

        //then
        assertNotNull(findBoard);
        assertEquals(findBoard.getTitle(), boardDTO.getTitle());

        List<BoardDTO> boardList = boardService.getBoardList(0, 1);
        System.out.println("boardList = " + boardList);
    }
}