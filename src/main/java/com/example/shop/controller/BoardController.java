package com.example.shop.controller;

import com.example.shop.dto.BoardDTO;
import com.example.shop.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public String boardList(Model model) {
        List<BoardDTO> boards = boardService.getBoardList(0, 10);
        model.addAttribute("boards", boards);
        return "board/boardList";
    }
}
