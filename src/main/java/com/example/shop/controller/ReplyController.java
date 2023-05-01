package com.example.shop.controller;

import com.example.shop.dto.ReplyDTO;
import com.example.shop.service.ReplyService;
import com.example.shop.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping
    public void register(@RequestBody ReplyDTO replyDTO) {
        replyDTO.setMemberId(SecurityUtil.getMemberIdByAuthentication());
        replyService.register(replyDTO);
    }

    @GetMapping("{boardId}")
    public List<ReplyDTO> getReplies(
            @PathVariable("boardId") Long boardId) {
        return replyService.findFromBoardId(boardId);
    }
}
