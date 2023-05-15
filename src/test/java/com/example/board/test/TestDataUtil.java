package com.example.board.test;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.MemberDTO;

public interface TestDataUtil {

    /**
     * 테스용 멤버 테스트DTO
     * memberDTO.setName("김지수");
     * memberDTO.setEmail("kimjisoo@test.com");
     * memberDTO.setPassword("1234");
     * memberDTO.setPhone("010-4953-3653");
     * memberDTO.setCity("서울시");
     * memberDTO.setStreet("강남구 지수네집");
     * memberDTO.setZipcode("08289");
     *
     * @return MemberDTO
     */
    static MemberDTO getTestMemberDTO() {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setName("김지수");
        memberDTO.setEmail("kimjisoo@test.com");
        memberDTO.setPassword("1234");
        memberDTO.setPhone("010-4953-3653");
        memberDTO.setCity("서울시");
        memberDTO.setStreet("강남구 지수네집");
        memberDTO.setZipcode("08289");
        return memberDTO;
    }

    static BoardDTO getTestBoardDTO() {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setCategoryName("질문");
        boardDTO.setContent("test");
        boardDTO.setTitle("content");
//        boardDTO.setMemberId();
        return boardDTO;
    }

}
