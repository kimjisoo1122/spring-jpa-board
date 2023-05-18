package com.example.board.dto;

import com.example.board.dto.common.BaseDto;
import com.example.board.entity.Reply;
import com.example.board.entity.enums.RecommendationStatus;
import com.example.board.util.SecurityUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class ReplyDto extends BaseDto {

    private Long id;
    private Long boardId;
    private Long memberId;
    private String content;
    private String memberName;
    private int recommendCnt;
    private RecommendationStatus recommendationStatus;
    private boolean isAuthor;

    public ReplyDto(Reply reply) {
        this.id = reply.getId();
        this.content = reply.getContent();
        this.boardId = reply.getBoard().getId();
        this.memberId = reply.getMember().getId();
        this.memberName = reply.getMember().getName();
        this.recommendCnt = reply.getRecommendCnt();
        this.createDate = reply.getCreateDate();
        this.updateDate = reply.getUpdateDate();
        this.isAuthor = Objects.equals(SecurityUtil.getMemberIdByAuthentication(), memberId);
    }
}