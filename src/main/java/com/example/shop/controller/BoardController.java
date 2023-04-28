package com.example.shop.controller;

import com.example.shop.config.security.CustomUserDetails;
import com.example.shop.dto.BoardDTO;
import com.example.shop.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    public static final String BOARD_HOME = "board/boardList";

    @GetMapping
    public String boardList(Model model) {
        List<BoardDTO> boards = boardService.getBoardList(0, 10);
        model.addAttribute("boards", boards);
        return BOARD_HOME;
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
        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boardDTO.setMemberId(userDetails.getMemberId());
        System.out.println("boardDTO = " + boardDTO);
        boardService.register(boardDTO);
        return BOARD_HOME;
    }
}
