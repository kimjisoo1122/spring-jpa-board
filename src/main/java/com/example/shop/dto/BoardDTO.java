package com.example.shop.dto;

import com.example.shop.dto.common.BaseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class BoardDTO extends BaseDTO {

    private Long id;
    private Long memberId;
    private String name;
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    @NotBlank(message = "카테고리를 선택해주세요.")
    private String categoryName;
    private String categoryParentName;
    private int viewCnt;
    private int recommendCnt;

    private List<ReplyDTO> replies;
    private int replyCnt;

    public BoardDTO(Long id,Long memberId, String name, LocalDateTime writeTime, String title, String content, String categoryName, int viewCnt, int recommendCnt) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
        this.createDate = writeTime;
        this.title = title;
        this.content = content;
        this.categoryName = categoryName;
        this.viewCnt = viewCnt;
        this.recommendCnt = recommendCnt;
    }
}
