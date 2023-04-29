package com.example.shop.controller;

import com.example.shop.dto.BoardDTO;
import com.example.shop.service.BoardService;
import com.example.shop.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public String boardList(Model model) {
        List<BoardDTO> boards = boardService.findBoardList(0, 10);
        model.addAttribute("boards", boards);
        return "board/boardList";
    }

    @GetMapping("/register")
    public String registerForm(@ModelAttribute("boardDTO") BoardDTO boardDTO) {
        return "board/boardForm";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute("boardDTO")
            @Valid BoardDTO boardDTO,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "board/boardForm";
        }
        boardDTO.setMemberId(SecurityUtil.getMemberIdByAuthentication());
        boardService.register(boardDTO);
        return "redirect:/board";
    }

    @GetMapping("/{boardId}")
    public String board(
            @PathVariable("boardId") Long boardId,
            Model model) {
        boardService.increaseViewCnt(SecurityUtil.getMemberIdByAuthentication(), boardId);
        BoardDTO boardDTO = boardService.findBoardDTOById(boardId);
        model.addAttribute("boardDTO", boardDTO);
        return "/board/board";
    }

    @PostMapping("/recommend/{type}/{boardId}")
    @ResponseBody
    public BoardDTO recommend(
            @PathVariable("type") String type,
            @PathVariable("boardId") Long boardId) {
        if (type.equals("add")) {
            boardService.addRecommendation(SecurityUtil.getMemberIdByAuthentication(), boardId);
        } else if (type.equals("remove")) {
            boardService.removeRecommendation(SecurityUtil.getMemberIdByAuthentication(), boardId);
        }
        return boardService.findBoardDTOById(boardId);
    }
}
