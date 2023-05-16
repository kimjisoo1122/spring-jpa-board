package com.example.board.dto;

import com.example.board.dto.common.BaseDto;
import com.example.board.entity.Reply;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReplyDto extends BaseDto {

    private Long id;
    private Long boardId;
    private Long memberId;
    private String content;
    private String memberName;

    public ReplyDto(Reply reply) {
        this.id = reply.getId();
        this.content = reply.getContent();
        this.boardId = reply.getBoard().getId();
        this.memberId = reply.getMember().getId();
        this.memberName = reply.getMember().getName();
        this.createDate = reply.getCreateDate();
        this.updateDate = reply.getUpdateDate();
    }
}