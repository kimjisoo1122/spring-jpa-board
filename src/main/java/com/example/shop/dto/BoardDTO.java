package com.example.shop.dto;

import com.example.shop.dto.common.BaseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
public class BoardDTO extends BaseDTO {

    private Long id;
    private Long memberId;
    private String name;
    private String title;
    private String content;
    private String categoryName;
    private int viewCnt;
    private int recommendCnt;

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
