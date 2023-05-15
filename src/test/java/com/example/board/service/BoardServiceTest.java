package com.example.board.service;

import com.example.board.dto.MemberDto;
import com.example.board.dto.board.BoardDto;
import com.example.board.entity.Board;
import com.example.board.entity.Category;
import com.example.board.entity.Member;
import com.example.board.repository.board.BoardRepository;
import com.example.board.repository.board.BoardViewRepository;
import org.junit.jupiter.api.BeforeEach;
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
    MemberService memberService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    BoardViewRepository boardViewRepository;

    BoardDto boardDto;
    MemberDto memberDto;

    @BeforeEach
    public void before() {
        boardDto = new BoardDto();
        boardDto.setCategoryName("질문");
        boardDto.setContent("내용");
        boardDto.setTitle("제목");

        memberDto = new MemberDto();
        memberDto.setName("김지수");
        memberDto.setEmail("kimjisoo@test.com");
        memberDto.setPassword("1234");
        memberDto.setPhone("010-4953-3653");
        memberDto.setCity("서울시");
        memberDto.setStreet("강남구 지수네집");
        memberDto.setZipcode("08289");
    }

    @Test
    @DisplayName("글등록")
    void register() throws Exception {
        // given
        Member member = createTestMember(memberDto);
        Category parentCategory = createTestCategory("커뮤니티", null);
        Category childCategory = createTestCategory("질문", parentCategory.getId());

        // when
        Board board = createTestBoard(member.getId(), childCategory.getName(), boardDto);

        // then
        assertThat(board.getTitle()).isEqualTo(boardDto.getTitle());
        assertThat(board.getContent()).isEqualTo(boardDto.getContent());
        assertThat(board.getMember().getName()).isEqualTo(member.getName());
        assertThat(board.getCategory().getName()).isEqualTo(childCategory.getName());
    }


    @Test
    void findById() throws Exception {
        // given
        Member member = createTestMember(memberDto);
        Category parentCategory = createTestCategory("커뮤니티", null);
        Category childCategory = createTestCategory("질문", parentCategory.getId());
        Board board = createTestBoard(member.getId(), childCategory.getName(), boardDto);

        // when
        BoardDto findBoardDto = boardService.findBoardDtoById(board.getId());

        // then
        assertThat(findBoardDto.getTitle()).isEqualTo(boardDto.getTitle());
        assertThat(findBoardDto.getContent()).isEqualTo(boardDto.getContent());
        assertThat(findBoardDto.getMemberName()).isEqualTo(member.getName());
        assertThat(findBoardDto.getCategoryName()).isEqualTo(childCategory.getName());
    }

    @Test
    void increaseViewCnt() throws Exception {
        // given
        Member member = createTestMember(memberDto);
        Category parentCategory = createTestCategory("커뮤니티", null);
        Category childCategory = createTestCategory("질문", parentCategory.getId());
        Board board = createTestBoard(member.getId(), childCategory.getName(), boardDto);

        // when
        boardService.increaseViewCnt(member.getId(), board.getId());


        // then
    }



    private Member createTestMember(MemberDto memberDto) {
        Long memberId = memberService.join(memberDto);
        Member member = memberService.findById(memberId).orElse(null);
        assertThat(member).isNotNull();
        return member;
    }

    private Category createTestCategory(String name, Long parentId) {
        Category category = Category.createCategory(name);
        categoryService.save(category, parentId);
        categoryService.findById(category.getId());
        return category;
    }

    private Board createTestBoard(Long memberId, String categoryName, BoardDto boardDto) {
        boardDto.setMemberId(memberId);
        boardDto.setCategoryName(categoryName);
        Long boardId = boardService.register(boardDto);
        Board board = boardRepository.findById(boardId).orElse(null);
        assertThat(board).isNotNull();
        return board;
    }
}