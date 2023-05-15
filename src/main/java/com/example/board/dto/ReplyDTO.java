package com.example.board.dto;

import com.example.board.dto.common.BaseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReplyDTO extends BaseDTO {

    private Long id;
    private Long boardId;
    private Long memberId;
    private String content;
    private String memberName;

    public ReplyDTO(Long id, String content, Long memberId, Long boardId, String memberName, LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.content = content;
        this.boardId = boardId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
}