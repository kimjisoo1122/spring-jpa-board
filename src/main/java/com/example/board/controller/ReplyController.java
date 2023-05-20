package com.example.board.controller;

import com.example.board.dto.ReplyDto;
import com.example.board.service.ReplyService;
import com.example.board.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping
    public void register(@RequestBody ReplyDto replyDTO) {
        if (StringUtils.hasText(replyDTO.getContent())) {
            replyDTO.setMemberId(SecurityUtil.getMemberIdByAuthentication());
            replyService.register(replyDTO);
        }
    }

    @GetMapping("{boardId}")
    public List<ReplyDto> getReplies(
            @PathVariable("boardId") Long boardId) {
        return replyService.findByBoardId(boardId, SecurityUtil.getMemberIdByAuthentication());
    }

    @PostMapping("/recommend/{type}/{replyId}")
    public ReplyDto recommend(
            @PathVariable("type") String type,
            @PathVariable("replyId") Long replyId) {

        Long memberId = SecurityUtil.getMemberIdByAuthentication();
        if (type.equals("add")) {
            replyService.addRecommendation(memberId, replyId);
        } else if (type.equals("remove")) {
            replyService.removeRecommendation(memberId, replyId);
        }
        return replyService.findReplyDtoById(replyId, memberId);
    }

    @DeleteMapping("/{replyId}")
    public int delete(@PathVariable("replyId") Long replyId) {
        return replyService.delete(replyId, SecurityUtil.getMemberIdByAuthentication());
    }

    @PutMapping()
    public void update(@RequestBody ReplyDto replyDto) {
        replyService.update(replyDto, SecurityUtil.getMemberIdByAuthentication());
    }
}
