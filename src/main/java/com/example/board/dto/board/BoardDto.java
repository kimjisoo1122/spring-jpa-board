package com.example.board.dto.board;

import com.example.board.dto.common.BaseDto;
import com.example.board.entity.Board;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class BoardDto extends BaseDto {

    private Long id;
    private Long memberId;
    private String memberName;
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    @NotBlank(message = "카테고리를 선택해주세요.")
    private String categoryName;
    private String categoryParentName;
    private int viewCnt;
    private int recommendCnt;
    private int replyCnt;


    public BoardDto(Board board) {
        this.id = board.getId();
        this.memberId = board.getMember().getId();
        this.memberName = board.getMember().getName();
        this.createDate = board.getCreateDate();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.categoryName = board.getCategory().getName();
        this.viewCnt = board.getViewCnt();
        this.recommendCnt = board.getRecommendCnt();
    }
}
