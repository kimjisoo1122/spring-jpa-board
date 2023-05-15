package com.example.board.repository.board;

import com.example.board.TestDataUtil;
import com.example.board.dto.MemberDto;
import com.example.board.dto.board.BoardDto;
import com.example.board.entity.Category;
import com.example.board.entity.Member;
import com.example.board.service.BoardService;
import com.example.board.service.CategoryService;
import com.example.board.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BoardViewRepositoryTest {

    @Autowired
    BoardViewRepository boardViewRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    BoardService boardService;
    @Autowired
    CategoryService categoryService;

    @Test
    @DisplayName("조회기록 저장")
    void save() throws Exception {
        // given
        MemberDto testMemberDTO = TestDataUtil.getTestMemberDTO();
        Long memberId = memberService.join(testMemberDTO);
        Member member = memberService.findById(memberId).orElse(null);
        assertThat(member).isNotNull();

        Category category = Category.createCategory("질문");
        categoryService.save(category, null);

        BoardDto testBoardDTO = TestDataUtil.getTestBoardDTO();
        testBoardDTO.setMemberId(member.getId());
        testBoardDTO.setCategoryName(category.getName());
        boardService.register(testBoardDTO);


        // when

        // then
    }

}