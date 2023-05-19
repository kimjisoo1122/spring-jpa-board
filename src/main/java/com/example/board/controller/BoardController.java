package com.example.board.controller;

import com.example.board.dto.board.BoardDto;
import com.example.board.dto.board.BoardSearchCondition;
import com.example.board.service.BoardService;
import com.example.board.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        Page<BoardDto> page = boardService.search(boardSearchCondition, pageable);
        model.addAttribute("page", page);
        return "board/boardList";
    }

    @GetMapping("/register")
    public String registerForm(
            @ModelAttribute("boardDto") BoardDto boardDto,
            Pageable pageable,
            Model model) {
        model.addAttribute("pageable", pageable);
        return "board/boardForm";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute("boardDto") @Valid BoardDto boardDto,
            BindingResult bindingResult,
            Pageable pageable,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "board/boardForm";
        }
        boardDto.setMemberId(SecurityUtil.getMemberIdByAuthentication());
        boardService.register(boardDto);
        redirectAttributes.addAttribute("page", pageable.getOffset());
        redirectAttributes.addAttribute("size", pageable.getPageSize());
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

        if (type.equals("add")) {
            boardService.addRecommendation(memberId, boardId);
        } else if (type.equals("remove")) {
            boardService.removeRecommendation(memberId, boardId);
        }
        return boardService.findBoardDtoById(boardId, memberId);
    }

    @DeleteMapping("/{boardId}")
    @ResponseBody
    public Pageable delete(
            @PathVariable("boardId") Long boardId,
            Pageable pageable,
            RedirectAttributes redirectAttributes) {
        boardService.delete(boardId, SecurityUtil.getMemberIdByAuthentication());
        return pageable;
    }

    @GetMapping("/update/{boardId}")
    public String updateForm(
            @PathVariable("boardId") Long boardId,
            Pageable pageable,
            Model model) {
        BoardDto boardDto = boardService.findBoardDtoById(boardId, SecurityUtil.getMemberIdByAuthentication());
        model.addAttribute("boardDto", boardDto);
        model.addAttribute("pageable", pageable);
        return "board/boardForm";
    }

    @PostMapping("/update")
    public String update(
            @ModelAttribute("boardDto") @Valid BoardDto boardDto,
            BindingResult bindingResult,
            Pageable pageable,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageable", pageable);
            return "board/boardForm";
        }
        boardService.update(boardDto);
        redirectAttributes.addAttribute("page", pageable.getOffset());
        redirectAttributes.addAttribute("size", pageable.getPageSize());
        return "redirect:/board";
    }
}
