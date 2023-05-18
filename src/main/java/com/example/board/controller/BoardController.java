package com.example.board.controller;

import com.example.board.dto.board.BoardDto;
import com.example.board.dto.board.BoardSearchCondition;
import com.example.board.entity.enums.RecommendationStatus;
import com.example.board.service.BoardService;
import com.example.board.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public String boardList(
            BoardSearchCondition boardSearchCondition,
            Pageable pageable,
            Model model) {
        Page<BoardDto> pages = boardService.search(boardSearchCondition, pageable);
        model.addAttribute("boards", pages.getContent());
        return "board/boardList";
    }

    @GetMapping("/register")
    public String registerForm(@ModelAttribute("boardDto") BoardDto boardDto) {
        return "board/boardForm";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute("boardDto")
            @Valid BoardDto boardDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "board/boardForm";
        }
        boardDto.setMemberId(SecurityUtil.getMemberIdByAuthentication());
        boardService.register(boardDto);
        return "redirect:/board";
    }

    @GetMapping("/{boardId}")
    public String board(
            @PathVariable("boardId") Long boardId,
            Model model) {

        Long memberId = SecurityUtil.getMemberIdByAuthentication();
        // 조회수
        boardService.increaseViewCnt(memberId, boardId);

        BoardDto boardDto = boardService.findBoardDtoById(boardId, memberId);
        model.addAttribute("boardDto", boardDto);
        return "/board/board";
    }

    @PostMapping("/recommend/{type}/{boardId}")
    @ResponseBody
    public BoardDto recommend(
            @PathVariable("type") String type,
            @PathVariable("boardId") Long boardId) {

        Long memberId = SecurityUtil.getMemberIdByAuthentication();

        RecommendationStatus recommendationStatus = RecommendationStatus.NOT_VOTED;
        if (type.equals("add")) {
            recommendationStatus = boardService.addRecommendation(memberId, boardId);
        } else if (type.equals("remove")) {
            recommendationStatus = boardService.removeRecommendation(memberId, boardId);
        }
        return boardService.findBoardDtoById(boardId, memberId);
    }
}
