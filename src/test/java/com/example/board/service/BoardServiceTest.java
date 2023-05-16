package com.example.board.service;

import com.example.board.TestDataUtil;
import com.example.board.dto.MemberDto;
import com.example.board.dto.board.BoardDto;
import com.example.board.entity.*;
import com.example.board.repository.board.BoardRecommendRepository;
import com.example.board.repository.board.BoardViewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BoardServiceTest {

    @Autowired
    BoardService boardService;
    @Autowired
    BoardViewRepository boardViewRepository;
    @Autowired
    BoardRecommendRepository boardRecommendRepository;
    @Autowired
    TestDataUtil testDataUtil;

    // 인스턴스변수들은 클래스가 메모리에 올라가고 차후 인스턴스생성때마다 할당
    BoardDto boardDto = TestDataUtil.getTestBoardDto();
    MemberDto memberDto = TestDataUtil.getTestMemberDto();


    @Test
    @DisplayName("글 등록")
    void register() throws Exception {
        // given
        Member member = testDataUtil.createTestMember(memberDto);
        Category parentCategory = testDataUtil.createTestCategory("커뮤니티", null);
        Category childCategory = testDataUtil.createTestCategory("질문", parentCategory.getId());

        // when
        Board board = testDataUtil.createTestBoard(member, childCategory, boardDto);

        // then
        assertThat(board.getTitle()).isEqualTo(boardDto.getTitle());
        assertThat(board.getContent()).isEqualTo(boardDto.getContent());
        assertThat(board.getMember().getName()).isEqualTo(member.getName());
        assertThat(board.getCategory().getName()).isEqualTo(childCategory.getName());
    }


    @Test
    @DisplayName("게시글 조회")
    void findById() throws Exception {
        // given
        Member member = testDataUtil.createTestMember(memberDto);
        Category parentCategory = testDataUtil.createTestCategory("커뮤니티", null);
        Category childCategory = testDataUtil.createTestCategory("질문", parentCategory.getId());
        Board board = testDataUtil.createTestBoard(member, childCategory, boardDto);

        // when
        BoardDto findBoardDto = boardService.findBoardDtoById(board.getId());

        // then
        assertThat(findBoardDto.getTitle()).isEqualTo(boardDto.getTitle());
        assertThat(findBoardDto.getContent()).isEqualTo(boardDto.getContent());
        assertThat(findBoardDto.getMemberName()).isEqualTo(member.getName());
        assertThat(findBoardDto.getCategoryName()).isEqualTo(childCategory.getName());
    }

    @Test
    @DisplayName("조회수 증가")
    void increaseViewCnt() throws Exception {
        // given
        Member member = testDataUtil.createTestMember(memberDto);
        Category parentCategory = testDataUtil.createTestCategory("커뮤니티", null);
        Category childCategory = testDataUtil.createTestCategory("질문", parentCategory.getId());
        Board board = testDataUtil.createTestBoard(member, childCategory, boardDto);

        // when
        boardService.increaseViewCnt(member.getId(), board.getId());

        // then
        assertThat(board.getViewCnt()).isEqualTo(1);
    }

    @Test
    @DisplayName("추천수 UP&DOWN")
    void recommend() throws Exception {
        // given
        Member member = testDataUtil.createTestMember(memberDto);
        Category parentCategory = testDataUtil.createTestCategory("커뮤니티", null);
        Category childCategory = testDataUtil.createTestCategory("질문", parentCategory.getId());
        Board board = testDataUtil.createTestBoard(member, childCategory, boardDto);

        // when
        boardService.addRecommendation(member.getId(), board.getId());
        BoardRecommendHistory boardRecommendHistory = boardRecommendRepository.findByMemberIdAndBoardId(member.getId(), board.getId()).orElse(null);

        // then
        assertThat(boardRecommendHistory).isNotNull();
        assertThat(boardRecommendHistory.getBoard()).isSameAs(board);
        assertThat(boardRecommendHistory.getMember()).isSameAs(member);
        assertThat(boardRecommendHistory.getStatus()).isEqualTo(RecommendationStatus.UP_VOTED);
        boardService.addRecommendation(member.getId(), board.getId());
        assertThat(boardRecommendHistory.getStatus()).isEqualTo(RecommendationStatus.NOT_VOTED);

        // when
        boardService.removeRecommendation(member.getId(), board.getId());
        assertThat(boardRecommendHistory.getStatus()).isEqualTo(RecommendationStatus.DOWN_VOTED);
        boardService.removeRecommendation(member.getId(), board.getId());
        assertThat(boardRecommendHistory.getStatus()).isEqualTo(RecommendationStatus.NOT_VOTED);
    }
}