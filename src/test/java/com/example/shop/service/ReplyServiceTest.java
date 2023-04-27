package com.example.shop.service;

import com.example.shop.dto.ReplyDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ReplyServiceTest {

    @Autowired
    ReplyService replyService;
    @Autowired
    BoardService boardService;
    @Autowired
    MemberService memberService;

    @Test
    void 댓글등록() throws Exception {
        //given
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setContent("test");
        replyDTO.setBoardId(1l);
        replyDTO.setMemberId(13l);
        replyDTO.setMemberName("김지수");
        //when
        Long replyId = replyService.register(replyDTO);
        //then
        List<ReplyDTO> fromBoardId = replyService.findFromBoardId(replyDTO.getBoardId());
        fromBoardId.forEach(System.out::println);
        Assertions.assertEquals(1, fromBoardId.size());
    }

}