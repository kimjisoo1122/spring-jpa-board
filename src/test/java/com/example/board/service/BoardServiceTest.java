package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.MemberDTO;
import com.example.board.entity.*;
import com.example.board.repository.BoardRecommendRepository;
import com.example.board.repository.BoardViewRepository;
import com.example.board.test.TestDataUtil;
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
    BoardViewRepository boardViewRepository;
    @Autowired
    BoardRecommendRepository boardRecommendRepository;

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
        boardService.increaseViewCnt(memberId, boardId);
        BoardViewHistory boardViewHistory = boardViewRepository.findByMemberIdAndBoardId(memberId, boardId).orElse(null);

        //then
        assertEquals(1, board.getViewCnt());
        assertNotNull(boardViewHistory);
    }

    @Test
    void 추천수() throws Exception {
        // given
        MemberDTO testMemberDTO = TestDataUtil.getTestMemberDTO();
        Long memberId = memberService.join(testMemberDTO);
        Member member = memberService.findById(memberId).orElse(null);
        assertNotNull(member);

        BoardDTO testBoardDTO = TestDataUtil.getTestBoardDTO();
        testBoardDTO.setMemberId(member.getId());
        Long boardId = boardService.register(testBoardDTO);
        Board board = boardService.findById(boardId).orElse(null);
        assertNotNull(board);
        // when
        boardService.addRecommendation(memberId, boardId);
        BoardRecommendHistory boardRecommendHistory = boardRecommendRepository.findByMemberIdAndBoardId(memberId, boardId).orElse(null);
        // then
        assertEquals(1, board.getRecommendCnt());
        assertNotNull(boardRecommendHistory);
        assertEquals(RecommendationStatus.UPVOTED, boardRecommendHistory.getStatus());

        // 감소 테스트
        // when
        boardService.removeRecommendation(memberId, boardId);
        // then
        assertEquals(-1, board.getRecommendCnt());
        assertEquals(RecommendationStatus.DOWNVOTED, boardRecommendHistory.getStatus());
    }
}