package com.example.board.repository.board;

import com.example.board.TestDataUtil;
import com.example.board.dto.MemberDto;
import com.example.board.dto.board.BoardDto;
import com.example.board.dto.board.BoardSearchCondition;
import com.example.board.entity.Board;
import com.example.board.entity.Category;
import com.example.board.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @PersistenceContext
    EntityManager em;

    BoardDto boardDto = TestDataUtil.getTestBoardDto();
    MemberDto memberDTO = TestDataUtil.getTestMemberDto();

    @Test
    @DisplayName("글등록")
    Board register() throws Exception {
        // given
        Member member = Member.createMember(memberDTO);
        em.persist(member);
        Category category = new Category("질문");
        em.persist(category);

        // when
        Board board = Board.createBoard(boardDto);
        board.setCategory(category);
        board.setMember(member);
        boardRepository.save(board);

        // then
        assertThat(board.getTitle()).isEqualTo(boardDto.getTitle());
        assertThat(board.getContent()).isEqualTo(boardDto.getContent());
        assertThat(board.getCategory().getName()).isEqualTo(category.getName());
        assertThat(board.getMember().getName()).isEqualTo(memberDTO.getName());

        return board;
    }

    @Test
    @DisplayName("검색조건으로 게시글 조회 ")
    void search() throws Exception {
        // given
        register();

        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setTitle("제목");
        condition.setContent("내");
        condition.setWriter(memberDTO.getName());

        // when
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<Board> pages = boardRepository.search(condition, pageRequest);

        // then
        assertThat(pages.getTotalPages()).isEqualTo(1);
    }
}