package com.example.board.controller;

import com.example.board.dto.ReplyDto;
import com.example.board.service.ReplyService;
import com.example.board.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping
    public void register(@RequestBody ReplyDto replyDTO) {
        replyDTO.setMemberId(SecurityUtil.getMemberIdByAuthentication());
        replyService.register(replyDTO);
    }

    @GetMapping("{boardId}")
    public List<ReplyDto> getReplies(
            @PathVariable("boardId") Long boardId) {
        return replyService.findByBoardId(boardId);
    }

    @PostMapping("/recommend/{type}/{replyId}")
    public ReplyDto recommend(
            @PathVariable("type") String type,
            @PathVariable("replyId") Long replyId) {

        if (type.equals("add")) {
            replyService.addRecommendation(SecurityUtil.getMemberIdByAuthentication(), replyId);
        } else if (type.equals("remove")) {
            replyService.removeRecommendation(SecurityUtil.getMemberIdByAuthentication(), replyId);
        }
        return replyService.findReplyDtoById(replyId);
    }
}
