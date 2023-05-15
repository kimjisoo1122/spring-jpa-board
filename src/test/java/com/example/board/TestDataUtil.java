package com.example.board;

import com.example.board.dto.board.BoardDto;
import com.example.board.dto.MemberDto;

public interface TestDataUtil {

    static MemberDto getTestMemberDTO() {
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

    static BoardDto getTestBoardDTO() {
        BoardDto boardDTO = new BoardDto();
        boardDTO.setCategoryName("질문");
        boardDTO.setContent("content");
        boardDTO.setTitle("title");
        return boardDTO;
    }

}
