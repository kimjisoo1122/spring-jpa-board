package com.example.board;

import com.example.board.dto.MemberDto;
import com.example.board.dto.board.BoardDto;
import com.example.board.entity.Board;
import com.example.board.entity.Category;
import com.example.board.entity.Member;
import com.example.board.service.BoardService;
import com.example.board.service.CategoryService;
import com.example.board.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;

// test클래스의 패키지가 main과 같다면 테스트 실행시에 컴포넌트대상이 됨
@Component
public class TestDataUtil {

    @Autowired
    MemberService memberService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    BoardService boardService;

    public Member createTestMember(MemberDto memberDto) {
        Long memberId = memberService.join(memberDto);
        Member member = memberService.findById(memberId).orElse(null);
        assertThat(member).isNotNull();
        return member;
    }

    public Category createTestCategory(String name, Long parentId) {
        Category category = Category.createCategory(name);
        categoryService.save(category, parentId);
        categoryService.findById(category.getId());
        return category;
    }

    public Board createTestBoard(Member member, Category category, BoardDto boardDto) {
        boardDto.setMemberId(member.getId());
        boardDto.setCategoryName(category.getName());
        Long boardId = boardService.register(boardDto);
        Board board = boardService.findById(boardId).orElse(null);
        assertThat(board).isNotNull();
        return board;
    }

    public static MemberDto getTestMemberDto() {
        MemberDto memberDTO = new MemberDto();
        memberDTO.setName("김지수");
        memberDTO.setEmail("kimjisoo@test.com");
        memberDTO.setPassword("1234");
        memberDTO.setPhone("010-4953-3653");
        memberDTO.setCity("서울시");
        memberDTO.setStreet("강남구 지수네집");
        memberDTO.setZipcode("08289");
        return memberDTO;
    }

    public static BoardDto getTestBoardDto() {
        BoardDto boardDTO = new BoardDto();
        boardDTO.setCategoryName("질문");
        boardDTO.setContent("내용");
        boardDTO.setTitle("제목");
        return boardDTO;
    }
}
