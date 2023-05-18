package com.example.board.controller;

import com.example.board.entity.enums.RecommendationStatus;
import com.example.board.service.ReplyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReplyController.class)
class ReplyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReplyService replyService;

    @Test
    @DisplayName("추천수")
    void recommmend() throws Exception {
        // given
        given(replyService.addRecommendation(any(), any())).willReturn(RecommendationStatus.UP_VOTED);

        // when
        mockMvc.perform(post("/recommend/{type}/{replyId}", "add", 1))
                .andExpect(status().isOk())
                .andDo(print());

        // then
    }
}