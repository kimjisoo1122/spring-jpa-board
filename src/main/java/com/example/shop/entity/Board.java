package com.example.shop.entity;

import com.example.shop.dto.BoardDTO;
import com.example.shop.entity.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private int viewCnt;
    private int recommendCnt;

    public static Board createBoard(BoardDTO boardDTO, Member member, Category category) {
        Board board = new Board();
        board.title = boardDTO.getTitle();
        board.content = boardDTO.getContent();
        board.member = member;
        board.category = category;
        board.viewCnt = 0;
        board.recommendCnt = 0;
        return board;
    }
}
