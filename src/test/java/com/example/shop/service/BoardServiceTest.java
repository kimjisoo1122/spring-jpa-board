package com.example.shop.service;

import com.example.shop.dto.BoardDTO;
import com.example.shop.dto.MemberDTO;
import com.example.shop.entity.Board;
import com.example.shop.entity.BoardViewHistory;
import com.example.shop.entity.Category;
import com.example.shop.entity.Member;
import com.example.shop.repository.BoardViewHistoryRepository;
import com.example.shop.test.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class BoardServiceTest {

    @Autowired
    BoardService boardService;
    @Autowired
    MemberService memberService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    BoardViewHistoryRepository boardViewHistoryRepository;

    @Test
    void 글등록() throws Exception {
        // given
        MemberDTO testMemberDTO = TestDataUtil.getTestMemberDTO();
        // when
        Long memberId = memberService.join(testMemberDTO);
        Member member = memberService.findById(memberId).orElse(null);
        // then
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
        // when
        categoryService.save(category1, parentCategoryId);
        Category findCategory = categoryService.findByName(category1.getName()).orElse(null);
        // then
        assertNotNull(findCategory);

        // given
        BoardDTO testBoardDTO = TestDataUtil.getTestBoardDTO();
        testBoardDTO.setMemberId(member.getId());

        // when
        Long boardId = boardService.register(testBoardDTO);
        Board findBoard = boardService.findById(boardId).orElse(null);

        // then
        assertNotNull(findBoard);
        assertEquals(findBoard.getTitle(), testBoardDTO.getTitle());
        List<BoardDTO> boardList = boardService.findBoardList(0, 1);
    }

    @Test
    void 조회수_증가() throws Exception {
        //given
        MemberDTO testMemberDTO = TestDataUtil.getTestMemberDTO();
        Long memberId = memberService.join(testMemberDTO);
        Member member = memberService.findById(memberId).orElse(null);
        assertNotNull(member);

        BoardDTO testBoardDTO = TestDataUtil.getTestBoardDTO();
        testBoardDTO.setMemberId(member.getId());
        Long boardId = boardService.register(testBoardDTO);
        Board board = boardService.findById(boardId).orElse(null);
        assertNotNull(board);

        //when
        boardService.increaseViewCnt(boardId, memberId);
        BoardViewHistory boardViewHistory = boardViewHistoryRepository.findByMemberIdAndBoardId(memberId, boardId).orElse(null);

        //then
        assertEquals(1, board.getViewCnt());
        assertNotNull(boardViewHistory);
    }
}